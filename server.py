from flask import Flask, request, Response
app = Flask(__name__)

@app.route("/")
def hello():
	resp = Response("hello")
	#
	#resp.headers['Access-Control-Allow-Origin'] = '*'
    #return "<html><head><title>hi</title></head><body>hello</body></html>"
	return resp

@app.route("/login", methods = ['POST', 'GET'])
def login():
	#test login credentials here
	if request.method == 'POST':
		return "POST"
	return "GET"

if __name__ == "__main__":
    app.run(host = '0.0.0.0')
