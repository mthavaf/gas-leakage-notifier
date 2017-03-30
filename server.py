from __future__ import  print_function
from flask import Flask, request, Response
from flaskext.mysql import MySQL
import json
import pymysql

app = Flask(__name__)
app.config['MYSQL_DATABASE_USER'] = 'root'
app.config['MYSQL_DATABASE_PASSWORD'] = 'CP@ijjub'
app.config['MYSQL_DATABASE_DB'] = 'GLN'
app.config['MYSQL_DATABASE_HOST'] = 'localhost'


@app.route("/")
def hello():
    resp = Response("hello")
    # resp.headers['Access-Control-Allow-Origin'] = '*'
    # return "<html><head><title>hi</title></head><body>hello</body></html>"
    return resp


@app.route("/update-numbers", methods=['POST'])
def update_numbers():
    if request.method == 'POST':

        unique_id = request.form['unique_id']
        number_1 = request.form['number_1']
        number_2 = request.form['number_2']
        number_3 = request.form['number_3']
        query = "UPDATE details SET ph_no_1 = '%s', ph_no_2 = '%s', ph_no_3 = '%s' WHERE id = '%s'" % (number_1, number_2, number_3, unique_id)

        if len(number_1) != 13 or len(number_2) != 13 or len(number_3) != 13:
            return json.dumps('{ "response":"error", "message":"Invalid numbers"}')
        mysql = MySQL()
        mysql.init_app(app)
        conn = mysql.connect()
        cur = conn.cursor(pymysql.cursors.DictCursor)
        cur.execute(query)
        conn.commit()
        cur.close()
        conn.close()
        return json.dumps('{ "response":"OK", "message":"updated"}')


@app.route("/login", methods=['POST'])
def login():

    if request.method == 'POST':
        user_name = request.form['user_name']
        password = request.form['password']

        mysql = MySQL()
        mysql.init_app(app)
        conn = mysql.connect()
        cur = conn.cursor(pymysql.cursors.DictCursor)
        query = "SELECT id FROM details WHERE (name = '%s' AND password = '%s')" % (user_name, password)
        cur.execute(query)
        conn.commit()
        returned_rows = cur.rowcount
        cur.close()
        conn.close()
        if returned_rows == 1:
            return json.dumps('{"response":"OK", "message":"%s"}' % cur.fetchone()["id"])
        return json.dumps('{"response":"error", "message":"username or password is incorrect"}')


@app.route("/primary-number/<unique_id>", methods=['GET'])
def get_primary_number(unique_id):
    if request.method == 'GET':
        # print(unique_id, file=sys.stderr)
        mysql = MySQL()
        mysql.init_app(app)
        conn = mysql.connect()
        cur = conn.cursor(pymysql.cursors.DictCursor)
        cur.execute("SELECT ph_no_p FROM details WHERE id = '%s'" % unique_id)
        response = json.dumps(cur.fetchone())
        conn.commit()
        cur.close()
        conn.close()
        return response


@app.route("/emergency-numbers/<unique_id>", methods=['GET'])
def get_emergency_numbers(unique_id):
    if request.method == 'GET':
        mysql = MySQL()
        mysql.init_app(app)
        conn = mysql.connect()
        cur = conn.cursor(pymysql.cursors.DictCursor)
        cur.execute("SELECT ph_no_1, ph_no_2, ph_no_3 FROM details WHERE id = '%s'" % unique_id)
        response = json.dumps(cur.fetchone())
        conn.commit()
        cur.close()
        conn.close()
        return response


@app.route("/numbers/<unique_id>", methods=['GET'])
def get_numbers(unique_id):
    if request.method == 'GET':
        mysql = MySQL()
        mysql.init_app(app)
        conn = mysql.connect()
        cur = conn.cursor(pymysql.cursors.DictCursor)
        cur.execute("SELECT ph_no_p, ph_no_1, ph_no_2, ph_no_3 FROM details WHERE id = '%s'" % unique_id)
        response = json.dumps(cur.fetchone())
        conn.commit()
        cur.close()
        conn.close()
        return response


if __name__ == "__main__":
    app.run(host='0.0.0.0')
