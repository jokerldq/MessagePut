import sqlite3
# conn=sqlite3.connect('用户.db')
# c=conn.cursor()
# c.execute('''CREATE TABLE users
#         (id INTEGER PRIMARY KEY autoincrement NOT NULL ,
#         username CHAR(50) NOT NULL ,
#         password CHAR(50) NOT NULL )''')
# print("Table created successfully")
# c.execute("SELECT * from users")
# for row in c:
#    print ("ID = ", row[0])
#    print ("NAME = ", row[1])
#    print ("TEACHER = ", row[2])
# conn.commit()
# conn.close()
# conn=sqlite3.connect('用户.db')
# c=conn.cursor()
# c.execute('''CREATE TABLE hello
#         (id INTEGER PRIMARY KEY autoincrement NOT NULL,
#          goodname CHAR(50) NOT NULL )''')
# c.execute("insert into hello (id,goodname) VALUES (1,'mike')")
# conn.commit()
# conn.close()
conn=sqlite3.connect('用户.db')
c=conn.cursor()
# c.execute('''CREATE TABLE message1
#         (id INTEGER PRIMARY KEY autoincrement NOT NULL,
#          message CHAR (100) NOT NULL )''')
# c.execute("insert into message1 (id,message) VALUES (1,'mike')")
c.execute('''CREATE TABLE message2
        (id INTEGER PRIMARY KEY autoincrement NOT NULL,
         username CHAR (100) NOT NULL ,
         message CHAR (100) NOT NULL )''')
c.execute("insert into message2 (id,username,message) VALUES (1,'mike','你好啊')")
# c.execute("SELECT * from message1")
# for row in c:
#    print ("ID = ", row[0])
#    print ("NAME = ", row[1])
conn.commit()
conn.close()