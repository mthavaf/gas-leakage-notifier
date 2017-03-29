from __future__ import  print_function
from flask import Flask, request, Response
from flaskext.mysql import MySQL
import sys
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


@app.route("/login", methods=['POST', 'GET'])
def login():

    # test login credentials here

    if request.method == 'POST':
        print(request.form['a'], file=sys.stderr)
        return json.dumps('{"message":"POST kanappa"}')
    return json.dumps('{"name":"ashwini","mode":"silent"}')


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
