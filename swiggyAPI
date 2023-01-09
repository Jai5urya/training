import json
import uuid
import cherrypy
from mysql.connector import connect
from datetime import datetime
import logging
import time


class Response(object):
    def wrong_input(self):
        result = {
            "status": "error",
            "code": "BAD_REQUEST",
            "message": "given userID is incorrect"
        }
        return result

    def invalid_request(self):
        result = {
            "status": "error",
            "code": "BAD_REQUEST",
            "message": "given request method is incorrect"
        }
        return result

    def invalid_parameter(self):
        result = {
            "status": "error",
            "code": "BAD_REQUEST",
            "message": "given request don't have the required parameters"
        }
        return result

    def data_not_found(self):
        result = {
            "status": "error",
            "code": "404-NOT_FOUND",
            "message": "data not found in db"
        }
        return result

    def unknown_error(self):
        result = {
            "status": "failure",
            "code": "UNKNOWN_ERROR",
            "message": "internal server error"
        }
        return result

    def unauthorized(self):
        result = {
            "status": "failure",
            "code": "UNAUTHORIZED",
            "message": "given oauth token is incorrect"
        }
        return result

    def incorrect_id(self):
        result = {
            "status": "failure",
            "code": "UNAUTHORIZED",
            "message": "given user_id or password is incorrect"
        }
        return result

    def invalid_coupon(self):
        result = {
            "status": "success",
            "code": "INVALID_COUPON",
            "message": "the given coupon is expired"
        }
        return result

    def delivery_unavailable(self):
        details = {
            "status": "success",
            "code": "DELIVERY_UNAVAILABLE",
            "message": "no delivery person available at this time. kindly try after some time"
        }
        return details

    def no_past_order(self):
        details = {
            "status": "success",
            "code": "NO-PAST-DATA",
            "message": "no past datas available"
        }
        return details


class Database(object):
    def __init__(self):
        self.db = connect(host="localhost", user="root", passwd="88888888", database="swiggy")

    def update_auth(self, auth_token, login_id, cur_time):
        cursor = self.db.cursor()
        query = "insert into authorization(auth_id,login_id,time) values(%s,%s,%s)"
        try:
            cursor.execute(query, (auth_token, login_id, cur_time))
            self.db.commit()
            cursor.reset()
        except Exception as e:
            print(e)
            return False
        return True

    def check_auth(self, auth_token):
        cursor = self.db.cursor()
        query = "select login_id from authorization where auth_id = %s"
        try:
            cursor.execute(query, (auth_token,))
            id = cursor.fetchone()[0]
            cursor.reset()
        except Exception as e:
            return None
        return id

    def clear_auth(self):
        cursor = self.db.cursor()
        query = "delete from authorization where time <= %s"
        try:
            print('a')
            cursor.execute(query, (round(time.time()),))
            self.db.commit()
        except Exception as e:
            return False
        return True

    def check_signin(self, email, phone_number):
        cursor = self.db.cursor()
        query1 = "select customer_id from customer where email = %s"
        query2 = "select customer_id from customer where phone_number = %s"
        try:
            cursor.execute(query1, (email,))
            details = cursor.fetchall()
            cursor.reset()
            if len(details) > 0:
                return True
            cursor.execute(query2, (phone_number,))
            details = cursor.fetchall()
            cursor.reset()
            if len(details) > 0:
                return True
        except Exception as e:
            print(e)
        return False

    def get_user_id(self, login_id):
        cursor = self.db.cursor()
        query = "select customer_id from customer where email = %s or phone_number = %s"
        try:
            cursor.execute(query, (login_id, login_id,))
            user_id = cursor.fetchone()[0]
            cursor.reset()
        except Exception as e:
            print(e)
            return None
        return user_id

    def check_login(self, login_id, password):
        is_number = True
        try:
            int(login_id)
        except Exception as e:
            is_number = False
        if is_number:
            query = "select customer_id from customer where phone_number = %s and password = MD5(%s)"
        else:
            query = "select customer_id from customer where email = %s and password = MD5(%s)"
        try:
            cursor = self.db.cursor()
            cursor.execute(query, (login_id, password))
            details = cursor.fetchall()
            cursor.reset()
            if len(details) == 0:
                return False
        except Exception as e:
            print(e)
            return False
        return True

    def update_address(self, landmark, locality, pincode, is_default, latitude, longitude, email):
        self.db = connect(host="localhost", user="root", passwd="88888888", database="swiggy")
        cursor = self.db.cursor()
        address = landmark + ", " + locality + ", " + pincode
        try:
            cursor.execute("select customer_id from customer where email=%s", (email,))
            customerID = cursor.fetchone()[0]
            cursor.reset()
            query = "insert into adress(full_address,landmark, locality, pincode, is_default, latitude, longitude," \
                    "customer_id) values(%s,%s,%s,%s,%s,%s,%s,%s)"
            cursor.execute(query,
                           (address, landmark, locality, pincode, bool(is_default), latitude, longitude, customerID,))
            self.db.commit()
        except Exception as e:
            print(e)
            return False
        return True

    def update_customer(self, user_name, email, password, phone_number, language) -> bool:
        query = "insert into customer(customer_name,password,phone_number,email,language) values(%s,MD5(%s),%s,%s,%s)"
        cursor = self.db.cursor()
        try:
            cursor.execute(query, (user_name, password, phone_number, email, language,))
            self.db.commit()
            cursor.execute("select customer_id from customer where email=%s", (email,))
            customerID = cursor.fetchone()[0]
            cursor.reset()
        except Exception as e:
            print(e)
            return None
        return customerID

    def updateRestaurant(self, user_id, name, is_functioning, location, pincode, latitude, longitude):
        address = location + ", " + pincode
        try:
            cursor = self.db.cursor()
            query = "insert into restaurant_details(name,is_functioning) values(%s,%s)"
            cursor.execute(query, (name, bool(is_functioning),))
            self.db.commit()
            query = "select last_insert_id()"
            cursor.execute(query)
            restaurant_id = cursor.fetchone()[0]
            cursor.reset()
            query = "insert into adress(full_address, locality, pincode, is_default, latitude, longitude," \
                    "customer_id, restaurant_id) values(%s,%s,%s,%s,%s,%s,%s,%s)"
            cursor.execute(query,
                           (address, location, pincode, True, latitude, longitude, user_id, restaurant_id,))
            self.db.commit()
            query = "select last_insert_id()"
            cursor.execute(query)
            address_id = cursor.fetchone()[0]
            cursor.reset()
            query = "update restaurant_details set address_id = %s where restaurant_id=%s "
            cursor.execute(query, (address_id, restaurant_id))
            self.db.commit()
        except Exception as e:
            print(e)
            return None
        return restaurant_id

    def update_food(self, restaurant_unique_id, food_category, food_name, food_price, is_veg, is_available):
        try:
            cursor = self.db.cursor()
            query = "insert into food_details(food_category, food_name, food_price, is_veg,is_available,rating_average) values(%s,%s,%s,%s,%s,%s) "
            cursor.execute(query, (food_category, food_name, food_price, bool(is_veg), bool(is_available), 4,))
            self.db.commit()
            query = "select last_insert_id()"
            cursor.execute(query)
            food_id = cursor.fetchone()[0]
            print(food_id)
            cursor.reset()
            query = "insert into restaurant_food(restaurant_id, food_id) values(%s,%s) "
            cursor.execute(query, (restaurant_unique_id, food_id,))
            self.db.commit()
        except Exception:
            print("err")
            return False
        return True

    def find_restaurant(self, name):
        print(1)
        query = "select restaurant_id from restaurant_details where name = %s"
        cursor = self.db.cursor()
        try:
            cursor.execute(query, (name,))
            id = cursor.fetchone()[0]
            print(id)
            cursor.reset()
        except Exception as e:
            return None
        if id is None:
            return None
        details = {
            "restaurant_id": id,
            "name": name
        }
        return details;

    def list_foods(self, restaurant_id):
        cursor = self.db.cursor()
        query = "select food_id , food_name from food_details where food_id in (select food_id from restaurant_food " \
                "where restaurant_id=%s) "
        try:
            cursor.execute(query, (restaurant_id,))
            row_headers = [x[0] for x in cursor.description]
            datas = cursor.fetchall()
            cursor.reset()
            json_data = []
            for result in datas:
                json_data.append(dict(zip(row_headers, result)))
        except Exception as e:
            print(e)
            return None
        return json_data;

    def get_restaurant(self, location, food_id, per_page):
        cursor = self.db.cursor()
        query = "select restaurant_details.name,restaurant_details.restaurant_id,adress.locality as location , " \
                "adress.latitude,adress.longitude, restaurant_details.is_functioning from restaurant_details join " \
                "adress on restaurant_details.address_id = adress.address_id where restaurant_details.restaurant_id " \
                "in (select restaurant_id from restaurant_food where food_id = %s) and adress.locality = %s; "
        try:
            cursor.execute(query, (food_id, location,))
        except Exception as e:
            print(e)
            return None
        row_headers = [x[0] for x in cursor.description]
        datas = cursor.fetchall()
        json_data = []
        for result in datas:
            json_data.append(dict(zip(row_headers, result)))
        more_records = True
        if len(datas) <= int(per_page):
            more_records = False
        details = {
            "details": {
                "restaurant": json_data
            },
            "meta": {
                "per_page": int(per_page),
                "more_records": more_records
            }
        }

        return details

    def delete_details(self, customer_id):
        query = "delete from customer where customer_id = %s"
        try:
            cursor = self.db.cursor()
            cursor.execute(query, (customer_id,))
            self.db.commit()
        except Exception as e:
            print(e)
            return False
        return True

    def remove_auth(self, token):
        cursor = self.db.cursor()
        query = "delete from authorization where auth_id = %s"
        try:
            cursor.execute(query, (token,))
            self.db.commit()
            cursor.close()
        except Exception as e:
            print(e)
            return True
        return False

    def get_foods(self, food_name):
        print(3)
        cursor = self.db.cursor()
        query = "select food_id from food_details where food_name = %s"
        try:
            cursor.execute(query, (food_name,))
            id = cursor.fetchone()[0]
            cursor.reset()
        except Exception as e:
            print(e)
            return "error"
        if id is None:
            return None
        result = {
            "name": food_name,
            "food_id": id
        }
        return result

    def get_pilot_id(self, customer_id):
        query = "select pilot_id from pilot_details where is_engaged = true"
        try:
            cursor = self.db.cursor()
            cursor.execute(query)
            pilot_id = cursor.fetchone()[0]
            cursor.reset()
            if pilot_id is None:
                return None
            query = "select phone_number from customer where customer_id = %s"
            cursor.execute(query, (customer_id,))
            phone_number = cursor.fetchone()[0]
            cursor.reset()
            query = "update pilot_details set is_engaged = false where pilot_id = %s"
            cursor.execute(query, (pilot_id,))
            self.db.commit()
            details = [pilot_id, phone_number]
        except Exception as e:
            print(e)
            return None
        return details

    def update_pilot(self, customer_id, licence_number, is_vaccinated, is_engaged):
        try:
            cursor = self.db.cursor()
            query = "insert into pilot_details(customer_id, license_number, is_vaccinated, is_engaged) values(%s,%s,%s," \
                    "%s) "
            cursor.execute(query, (customer_id, licence_number, bool(is_vaccinated), bool(is_engaged),))
            self.db.commit()
            print(customer_id)
            query = "select last_insert_id()"
            cursor.execute(query)
            pilot_id = cursor.fetchone()[0]
            print(pilot_id)
            cursor.reset()
        except Exception as e:
            print(e)
            return None
        return pilot_id

    def search_restaurant(self, food_id):
        cursor = self.db.cursor()
        query = "select restaurant_id , name from restaurant_details where restaurant_id in (select restaurant_id " \
                "from restaurant_food where food_id=%s) "
        try:
            cursor.execute(query, (food_id,))
            row_headers = [x[0] for x in cursor.description]
            datas = cursor.fetchall()
            json_data = []
            for result in datas:
                json_data.append(dict(zip(row_headers, result)))
            cursor.reset()
        except Exception as e:
            print(e)
            return "error"
        return json_data

    def check_coupon(self, food_id, order_value, code):
        query = "select discount_value from coupon_code where min_value <= %s and food_id = %s and code_name = %s"
        try:
            cursor = self.db.cursor()
            cursor.execute(query, (order_value, food_id, code))
            discount = cursor.fetchone()[0]
            cursor.reset()
        except Exception as e:
            print(e)
            return None
        return discount

    def place_order(self, food_id, pilot_id, customer_id, restaurant_id):
        # print(food_id, pilot_id, customer_id, restaurant_id)
        query = "insert into orders (is_delivered,is_liked_order,delivery_rating,foo_rating,food_id, pilot_id,customer_id, restaurant_id) values(%s,%s,%s,%s,%s,%s,%s,%s) "
        try:
            cursor = self.db.cursor()
            cursor.execute(query, (True, True, 4, 4, food_id, pilot_id, customer_id, restaurant_id,))
            self.db.commit()
            cursor.reset()
        except Exception as e:
            print(e)
            return True
        return False

    def get_past_orders(self, id):
        query = "select restaurant_id , food_id , order_id from orders where customer_id = %s"
        try:
            cursor = self.db.cursor()
            cursor.execute(query, (id,))
            row_headers = [x[0] for x in cursor.description]
            datas = cursor.fetchall()
            if datas[0] is None:
                return None
            cursor.reset()
            json_data = []
            for result in datas:
                json_data.append(dict(zip(row_headers, result)))
        except Exception as e:
            print(e)
            return None
        return json_data

    def get_liked_foods(self, id):
        query = "select restaurant_id , food_id , order_id from orders where customer_id = %s and is_liked_order = 1"
        try:
            cursor = self.db.cursor()
            cursor.execute(query, (id,))
            row_headers = [x[0] for x in cursor.description]
            datas = cursor.fetchall()
            if datas[0] is None:
                return None
            cursor.reset()
            json_data = []
            for result in datas:
                json_data.append(dict(zip(row_headers, result)))
        except Exception as e:
            print(e)
            return None
        return json_data

    def get_sorted_foods(self, keyword, restaurant_id):
        query = "select food_details.food_id,food_name,food_price from food_details join restaurant_food on " \
                "food_details.food_id = restaurant_food.food_id where restaurant_food.restaurant_id = %s and " \
                "food_details.is_veg = %s "
        is_veg = True
        if keyword == "non-veg":
            is_veg = False
        try:
            cursor = self.db.cursor()
            cursor.execute(query, (restaurant_id, bool(is_veg),))
            row_headers = [x[0] for x in cursor.description]
            datas = cursor.fetchall()
            if len(datas) == 0:
                return "no food available"
            if datas[0] is None:
                return None
            cursor.reset()
            json_data = []
            for result in datas:
                json_data.append(dict(zip(row_headers, result)))
        except Exception as e:
            print(e, "kk")
            return None
        return json_data

    def update_rating(self, order_id, food_rating, delivery_rating):
        query = "update orders set is_liked_order = 1 , delivery_rating = %s , foo_rating = %s where order_id = %s"
        try:
            cursor = self.db.cursor()
            cursor.execute(query, (delivery_rating, food_rating, order_id,))
            self.db.commit()
        except Exception as e:
            print(e)
            return True
        return False

    def schedule_deletion(self, id):
        valid_upto = round(time.time() + (15 * 24 * 3600))
        query = "insert into deletion_scheduler(valid_upto , is_active, customer_id) values(%s,%s,%s)"
        try:
            cursor = self.db.cursor()
            cursor.execute(query, (valid_upto, 0, id,))
            self.db.commit()
        except Exception as e:
            print(e)
            return True
        return False

    def clear_scheduler(self):
        current_time = round(time.time())
        query = "select customer_id from deletion_scheduler where is_active=0 and valid_upto < %s"
        try:
            cursor = self.db.cursor()
            cursor.execute("delete from deletion_scheduler where is_active = 1")
            self.db.commit()
            cursor.execute(query, (current_time))
            datas = cursor.fetchall()
            cursor.reset()
        except Exception as e:
            print(e)
            return True
        for id in datas:
            self.delete_details(id)
        return False

    def update_scheduler(self, user_id):
        query = "update deletion_scheduler set is_active = 1 where customer_id = %s"
        try:
            cursor = self.db.cursor()
            cursor.execute(query, (user_id,))
            self.db.commit()
        except Exception as e:
            print(e)
            return True
        return False

    def update_notification(self, restaurant_id, pilot_id):
        query = "insert into notification (restaurant_id, pilot_id) values(%s,%s)"
        try:
            cursor = self.db.cursor()
            cursor.execute(query,(restaurant_id, pilot_id))
            self.db.commit()
        except Exception as e:
            print(e)
            return True
        return False


class Food(object):
    def __init__(self):
        self.response_obj = Response()
        self.database_obj = Database()

    @cherrypy.expose
    def index(self):
        return "welcomeeee"

    @cherrypy.tools.json_out()
    @cherrypy.expose
    def add(self, restaurant_unique_id=None, food_category=None, food_name=None, food_price=None, is_veg=None,
            is_available=None):
        if not self.database_obj.clear_auth():
            print("unknown")
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'PUT':
            return self.response_obj.invalid_request()
        if None in [restaurant_unique_id, food_category, food_name, food_price, is_veg, is_available]:
            return self.response_obj.invalid_parameter()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        print("checking")
        id = self.database_obj.check_auth(token)
        if id is None:
            print("iid is none")
            return self.response_obj.unauthorized()
        if not self.database_obj.update_food(restaurant_unique_id, food_category, food_name, food_price, is_veg,
                                             is_available):
            return self.response_obj.unknown_error()
        return {
            "status": "success",
            "code": "SUCCESS"
        }

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def apply_coupon(self, food_id=None, order_value=None, code=None):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'POST':
            return self.response_obj.invalid_request()
        if None in [food_id, order_value, code]:
            return self.response_obj.invalid_parameter()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        id = self.database_obj.check_auth(token)
        if id is None:
            return self.response_obj.unauthorized()
        discount = self.database_obj.check_coupon(food_id, order_value, code)
        if discount is None:
            return self.response_obj.invalid_coupon()
        return {
            "status": "success",
            "code": "SUCCESS",
            "details": {
                "discount_amount": discount
            }
        }

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def order(self, food_ids=None, restaurant_id=None):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'PUT':
            return self.response_obj.invalid_request()
        if None in [food_ids, restaurant_id]:
            return self.response_obj.invalid_parameter()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        customer_id = self.database_obj.check_auth(token)
        if customer_id is None:
            return self.response_obj.unauthorized()
        # if None in[food_ids,restaurant_id ]
        order_data = json.loads(food_ids)
        food_id = order_data["food_id"]
        pilot_details = self.database_obj.get_pilot_id(customer_id)
        if pilot_details is None:
            return self.response_obj.delivery_unavailable()
        if self.database_obj.place_order(food_id, pilot_details[0], customer_id, restaurant_id):
            return self.response_obj.unknown_error()
        details = {
            "delivery_id": pilot_details[0],
            "delivery_phone_number": pilot_details[1]
        }
        return {
            "status": "sucess",
            "code": "SUCCESS",
            "details": details
        }

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def rating(self, order_id=None, food_rating=None, delivery_rating=None):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'PUT':
            return self.response_obj.invalid_request()
        if None in [order_id, food_rating, delivery_rating]:
            return self.response_obj.invalid_parameter()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        customer_id = self.database_obj.check_auth(token)
        if customer_id is None:
            return self.response_obj.unauthorized()
        if self.database_obj.update_rating(order_id, food_rating, delivery_rating):
            return self.response_obj.unknown_error()
        return {
            "status": "success",
            "code": "SUCCESS",
            "msg": "updated successfully"
        }


class Restaurant(object):
    def __init__(self):
        self.database_obj = Database()
        self.response_obj = Response()

    @cherrypy.expose
    def index(self):
        return "hello"

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def add(self, name=None, is_functioning=None, location=None, pincode=None, latitude=None, longitude=None):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'PUT':
            return self.response_obj.invalid_request()
        if None in [name, is_functioning, location, pincode, latitude, longitude]:
            return self.response_obj.invalid_parameter()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        id = self.database_obj.check_auth(token)
        if id is None:
            return self.response_obj.unauthorized()
        restaurant_id = self.database_obj.updateRestaurant(id, name, is_functioning, location, pincode, latitude,
                                                           longitude)
        return {
            "status": "success",
            "code": "SUCCESS",
            "details": {
                "msg": "restaurant details updated successfully",
                "restaurant_id": restaurant_id
            }
        }

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def notify(self, restaurant_id, pilot_id):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'PUT':
            return self.response_obj.invalid_request()
        if None in [restaurant_id, pilot_id]:
            return self.response_obj.invalid_parameter()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        id = self.database_obj.check_auth(token)
        if id is None:
            return self.response_obj.unauthorized()
        if self.database_obj.update_notification(restaurant_id, pilot_id):
            return self.response_obj.unknown_error()
        return {
            "status": "success",
            "code": "SUCCESS",
            "msg": "notification sent successfully"
        }


class User(object):
    def __init__(self):
        self.database_obj = Database()
        self.response_obj = Response()

    @cherrypy.expose
    def index(self):
        return "welcome to user api !!"

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def signup(self, user_name=None, email=None, password=None, phone_number=None, language=None, landmark=None,
               locality=None, pincode=None, is_default=None, latitude=None, longitude=None):
        if None in [user_name, email, password, phone_number, language, landmark, locality, pincode, is_default,
                    latitude, longitude]:
            return self.response_obj.invalid_request()
        if cherrypy.request.method != 'POST':
            return self.response_obj.invalid_request()
        is_already_present = self.database_obj.check_signin(email, phone_number)
        if is_already_present:
            return {
                "status": "failure",
                "code": "409-conflict",
                "msg": "mail_id or phone_number already present"
            }
        user_id = self.database_obj.update_customer(user_name, email, password, phone_number, language)
        if user_id is None:
            return self.response_obj.unknown_error()
        is_updated = self.database_obj.update_address(landmark, locality, pincode, is_default, latitude, longitude,
                                                      email)
        if not is_updated:
            return self.response_obj.unknown_error()
        details = {
            "user_id": str(user_id),
        }
        logging.info('signup updated successfully')
        return {"status": "success", "code": "SUCCESS", "details": details}

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def login(self, email_or_phone_number=None, password=None):
        try:
            self.database_obj.clear_scheduler()
        except Exception as e:
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'POST':
            return self.response_obj.invalid_request()
        if None in [email_or_phone_number, password]:
            return self.response_obj.invalid_parameter()
        is_logged_in = self.database_obj.check_login(email_or_phone_number, password)
        if not is_logged_in:
            return self.response_obj.incorrect_id()
        auth_token = str(uuid.uuid4())
        now = datetime.now()
        current_time = now.strftime("%H:%M:%S")
        user_id = self.database_obj.get_user_id(email_or_phone_number)
        if user_id is None:
            return self.response_obj.unknown_error()
        if self.database_obj.update_scheduler(user_id):
            return self.response_obj.unknown_error()
        valid_upto = time.time() + (30 * 60)
        self.database_obj.update_auth(auth_token, user_id, valid_upto)
        return {
            "status": "success",
            "code": "SUCCESS",
            "details": {
                "login_time": current_time,
                "user_id": user_id,
                "auth_token": auth_token
            }
        }

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def listRestaurant(self, food_id=None, per_page=None, location=None):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'GET':
            return self.response_obj.invalid_request()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        id = self.database_obj.check_auth(token)
        if id is None:
            return self.response_obj.unauthorized()
        if None in [location, food_id, per_page]:
            return self.response_obj.invalid_parameter()
        details = self.database_obj.get_restaurant(location, food_id, per_page)
        if details is None:
            return self.response_obj.unknown_error()
        return {
            "status": "success",
            "code": "SUCCESS",
            "details": details
        }

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def logout(self):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'DELETE':
            return self.response_obj.invalid_request()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        id = self.database_obj.check_auth(token)
        if id is None:
            return self.response_obj.unauthorized()
        if self.database_obj.remove_auth(token):
            return self.response_obj.unknown_error()
        return {
            "status": "success",
            "code": "SUCCESS",
            "msg": "logged out successfully"
        }

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def delete(self):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'DELETE':
            return self.response_obj.invalid_request()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        id = self.database_obj.check_auth(token)
        if id is None:
            return self.response_obj.unauthorized()
        is_deleted = self.database_obj.delete_details(id)
        if not is_deleted:
            return self.response_obj.unknown_error()
        if self.database_obj.remove_auth(token):
            return self.response_obj.unknown_error()
        return {
            "status": "success",
            "code": "SUCCESSS"
        }

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def register(self, customer_id=None, licence_number=None, is_vaccinated=None, is_engaged=None):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'PUT':
            return self.response_obj.invalid_request()
        if None in [customer_id, licence_number, is_vaccinated, is_engaged]:
            return self.response_obj.invalid_parameter()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        id = self.database_obj.check_auth(token)
        if id is None:
            return self.response_obj.unauthorized()
        if id != customer_id:
            return self.response_obj.wrong_input()
        pilot_id = self.database_obj.update_pilot(customer_id, licence_number, is_vaccinated, is_engaged)
        if pilot_id is None:
            return self.response_obj.unknown_error()
        return {
            "status": 'success',
            "code": "SUCCESS",
            "details": {
                "pilot_id": pilot_id
            }
        }

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def liked_foods(self):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'GET':
            return self.response_obj.invalid_request()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        id = self.database_obj.check_auth(token)
        if id is None:
            return self.response_obj.unauthorized()
        details = self.database_obj.get_liked_foods(id)
        if details is None:
            return self.response_obj.no_past_order()
        return {
            "status": "success",
            "code": "SUCCESS",
            "details": details
        }

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def past_orders(self):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'GET':
            return self.response_obj.invalid_request()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        id = self.database_obj.check_auth(token)
        if id is None:
            return self.response_obj.unauthorized()
        details = self.database_obj.get_past_orders(id)
        print(details)
        if details is None:
            return self.response_obj.unknown_error()
        return {
            "status": "success",
            "code": "SUCCESS",
            "details": details
        }

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def schedule_deletion(self):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'DELETE':
            return self.response_obj.invalid_request()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        id = self.database_obj.check_auth(token)
        if id is None:
            return self.response_obj.unauthorized()
        if self.database_obj.schedule_deletion(id):
            return self.response_obj.unknown_error()
        return {
            "status": "success",
            "code": "SUCCESS",
            "msg": "scheduled for deletion"
        }


class Search(object):
    def __init__(self):
        self.database_obj = Database()
        self.response_obj = Response()

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def search(self, search_for=None, query=None):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'GET':
            return self.response_obj.invalid_request()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        id = self.database_obj.check_auth(token)
        if id is None:
            return self.response_obj.unauthorized()
        if None in [search_for, query]:
            return self.response_obj.invalid_parameter()
        result = None
        try:
            if search_for == 'food':
                result = self.searchFood(query)
            elif search_for == 'restaurant':
                result = self.searchRestaurant(query)
            else:
                return self.response_obj.invalid_parameter()
        except Exception as e:
            return self.response_obj.unknown_error()
        return {
            "status": "success",
            "code": "SUCCESS",
            "details": result
        }

    def searchFood(self, food_name):
        food = self.database_obj.get_foods(food_name)
        if food_name is None:
            return self.response_obj.data_not_found()
        restaurant = self.database_obj.search_restaurant(food["food_id"])
        if restaurant is None:
            return self.response_obj.data_not_found()
        details = {
            "food": food,
            "restaurant": restaurant
        }
        return {
            "status": "success",
            "code": "SUCCESS",
            "details": details
        }

    def searchRestaurant(self, name):
        restaurant = self.database_obj.find_restaurant(name)
        print(restaurant)
        if restaurant is None:
            return self.response_obj.data_not_found()
        food = self.database_obj.list_foods(restaurant["restaurant_id"])
        details = {
            "restaurant": restaurant,
            "food": food,
        }
        return {
            "status": "success",
            "code": "SUCCESS",
            "details": details
        }

    @cherrypy.expose
    @cherrypy.tools.json_out()
    def sort(self, keyword=None, restaurant_id=None):
        if not self.database_obj.clear_auth():
            return self.response_obj.unknown_error()
        if cherrypy.request.method != 'GET':
            return self.response_obj.invalid_request()
        auth_header = cherrypy.request.headers.get('Authorization')
        if auth_header is None:
            return self.response_obj.unauthorized()
        token = auth_header[7:]
        id = self.database_obj.check_auth(token)
        if id is None:
            return self.response_obj.unauthorized()
        if None in [keyword, restaurant_id]:
            return self.response_obj.invalid_parameter()
        details = self.database_obj.get_sorted_foods(keyword, restaurant_id)
        if details is None:
            return self.response_obj.unknown_error()
        return {
            "status": "success",
            "code": "SUCCESS",
            "details": details
        }


class Welcome(object):
    @cherrypy.expose
    def index(self):
        return "welcome to swiggy api!!"


if __name__ == '__main__':
    # API tree
    myURL = Welcome()
    myURL.swiggy = Welcome()
    myURL.swiggy.user = User()
    myURL.swiggy.user.login = User().login
    myURL.swiggy.user.signup = User().signup
    myURL.swiggy.user.logout = User().logout
    myURL.swiggy.user.delete = User().delete
    myURL.swiggy.user.schedule_deletion = User().schedule_deletion

    myURL.swiggy.user.listRestaurant = User().listRestaurant
    myURL.swiggy.user.liked_foods = User().liked_foods
    myURL.swiggy.user.past_orders = User().past_orders

    myURL.swiggy.search = Search().search
    myURL.swiggy.sort = Search().sort

    myURL.swiggy.food = Food()
    myURL.swiggy.food.add = Food().add
    myURL.swiggy.food.order = Food().order
    myURL.swiggy.food.rating = Food().rating

    myURL.swiggy.pilot = User()
    myURL.swiggy.pilot.register = User().register

    myURL.swiggy.food.apply_coupon = Food().apply_coupon

    myURL.swiggy.restaurants = Restaurant()
    myURL.swiggy.restaurants.notify = Restaurant().notify
    myURL.swiggy.restaurants.add = Restaurant().add

    conf = {
        '/': {
            'tools.sessions.on': True
        }
    }
    cherrypy.tree.mount(myURL, '/', conf)
    cherrypy.server.socket_host = "127.0.0.1"
    cherrypy.engine.start()
