# For reading data from ADC
import Adafruit_GPIO.SPI as SPI
import Adafruit_MCP3008

# for geting time in seconds
import time


# For publishing into app
import paho.mqtt.client as mqtt

# For sending sms
from twilio.rest import TwilioRestClient

# http
import urllib
import httplib
import json

STATE_IS_OK = True
STATE_IS_EMERGENCY = False
last_time = 0
PRIMARY = 1
EMERGENCY = 2
value = 0

connected = False

# twilio
ACCOUNT_SID = "AC10681973e6b0d4d1cb44109f97a305e4"  # Mentioned in twilip account
AUTH_TOKEN = "a7d5101a34ec4f0e42d8a174a6fb7154"  # Mentioned in twilio account
tclient = TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN)  # Twilip client object
twilio_number = "+16187084599"

conn = httplib.HTTPConnection("192.168.43.46:5000")

conn.request("GET", "/numbers/ABCD1234")
r1 = conn.getresponse()
d1 = r1.read()
data = json.loads(d1)
print(data)


def on_disconnect(client, userdata, rc):
    global connected
    connected = True


def publish(value):
    global connected
    if connected:
        print(value)
        client.publish("ABCD1234", value)


def sms(number):
    tclient.messages.create(body="Gas leakage detected. Gas level percentage is " +
                            str(float(value)*100/1024), to=number, from_=twilio_number)


def send_sms(NUMBER):
    read_again(0)
    if NUMBER == PRIMARY:
        sms(data["ph_no_p"])
    if NUMBER == EMERGENCY:
        sms(data["ph_no_1"])
        sms(data["ph_no_2"])
        sms(data["ph_no_3"])


# update phone numbers in case of user upgradation in the app
def read_again(dummy):
    global conn, r1, d1, data
    conn.request("GET", "/numbers/ABCD1234")
    r1 = conn.getresponse()
    d1 = r1.read()
    data = json.loads(d1)
    print(data)


# ADC pins
CLK = 18
MISO = 23
MOSI = 24
CS = 25
# ADC object creation
mcp = Adafruit_MCP3008.MCP3008(clk=CLK, cs=CS, miso=MISO, mosi=MOSI)


# Mqtt object creation and connecting
client = mqtt.Client()
client.on_connect = on_connect
client.connect("iot.eclipse.org", 1883)
client.loop_start()


while True:
    value = mcp.read_adc(0)
    # print str(value) + "test"
    # value=(float(value)/1024)*100
    current_time = time.time()
    time.sleep(2)
    publish(value)
    # primary SMS logic

    if value > 500 and STATE_IS_OK:
        print("Leakage occured")
        publish(-1)
        send_sms(PRIMARY)
        STATE_IS_OK = False
        STATE_IS_EMERGENCY = True
        last_time = current_time
    # emergency SMS logic

    if value > 500 and STATE_IS_EMERGENCY and (current_time - last_time) > 5:
        print("Critical stage")
        send_sms(EMERGENCY)
        STATE_IS_OK = True
        STATE_IS_EMEGENCY = False
        last_time = 0
