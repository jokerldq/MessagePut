import wx
import telnetlib
import sqlite3
from time import sleep
import _thread as thread

import os
import socket
import sys
import time
import threading
class Mainwindow(wx.Frame):
    def __init__(self, superior):
        wx.Frame.__init__(self, parent=superior, title="即时聊天系统登录", size=(500, 500))
        panel = wx.Panel(self)

        self.Center()
        wx.StaticText(parent=panel, label='用户名:', pos=(100, 100))
        wx.StaticText(parent=panel, label='密码:', pos=(100, 200))
        self.input = wx.TextCtrl(parent=panel, pos=(150, 95))
        self.input5 = wx.TextCtrl(parent=panel, pos=(150, 195), style=wx.TE_PASSWORD)
        self.buttonCheck = wx.Button(parent=panel, label='登录', pos=(120, 300))
        self.buttonCheck1 = wx.Button(parent=panel, label='注册', pos=(250, 300))
        self.Bind(wx.EVT_BUTTON, self.OnButtonCheck, self.buttonCheck)
        self.Bind(wx.EVT_BUTTON, self.GoToRegister, self.buttonCheck1)
    def GoToRegister(self,event):
        self.Close()
        newwindow=RegisterWindow(None)
        newwindow.Show(True)
    def OnButtonCheck(self, event):
        global use
        use=self.input.GetValue()
        user = (self.input.GetValue(),)
        conn = sqlite3.connect('用户.db')
        cur = conn.execute('select * from users where username=?', user)
        res = cur.fetchone()
        conn.close()
        if res is None:
            wx.MessageBox('用户不存在')
        else:
            if res[2] != self.input5.GetValue():
                wx.MessageBox('密码不正确')
            else:
                wx.MessageBox('成功!')
                con.open(host="127.0.0.1", port=5510, timeout=10)
                response = con.read_some()
                if response != b'Connect Success':
                    return
                con.write(('login ' + str(self.input.GetLineText(0)) + '\n').encode("utf-8"))
                self.Close()
                NewWindow = MainWindow2(None)
                NewWindow.Show(True)


class RegisterWindow(wx.Frame):
    def __init__(self, superior):
        wx.Frame.__init__(self, parent=superior, title="即时聊天系统注册", size=(600, 600))
        panel = wx.Panel(self)
        self.Center()
        wx.StaticText(parent=panel, label='用户名:', pos=(100, 100))
        wx.StaticText(parent=panel, label='密码:', pos=(110, 150))
        wx.StaticText(parent=panel, label='确认密码:', pos=(85, 200))
        self.input1 = wx.TextCtrl(parent=panel, pos=(150, 95))
        self.input2 = wx.TextCtrl(parent=panel, pos=(150, 145), style=wx.TE_PASSWORD)
        self.input3 = wx.TextCtrl(parent=panel, pos=(150, 195), style=wx.TE_PASSWORD)
        self.buttonCheck3 = wx.Button(parent=panel, label='注册', pos=(120, 300))
        self.Bind(wx.EVT_BUTTON, self.RegisterCheck, self.buttonCheck3)

    def RegisterCheck(self, event):
        us = (self.input1.GetValue(),)
        pasw = (self.input2.GetValue(),)
        pasw1 = (self.input3.GetValue(),)
        pasw2 = (self.input1.GetValue(),self.input2.GetValue())
        conn = sqlite3.connect('用户.db')
        cur = conn.execute('select * from users where username=?', us)
        res = cur.fetchone()
        conn.close()
        if res is not None:
            wx.MessageBox('用户已存在')
        else:
            if pasw != pasw1:
                wx.MessageBox('密码不一致')
            else:
                con=sqlite3.connect('用户.db')
                con.execute("insert into users(username,password) values(?,?)",pasw2)
                con.commit()
                con.close()
                wx.MessageBox('注册成功！')
                self.Close()
                NewWindow=Mainwindow(None)
                NewWindow.Show(True)

class MainWindow2(wx.Frame):
    def __init__(self, superior):
        wx.Frame.__init__(self, parent=superior, title="即时聊天系统", size=(800, 800))
        panel = wx.Panel(self)
        self.Center()
        self.input1 = wx.TextCtrl(parent=panel, pos=(50, 500), size=(600, 150))
        self.buttonCheck = wx.Button(parent=panel, label='发送', pos=(620, 680))
        self.listbox = wx.TextCtrl(panel,  pos=(50, 100), size=(600, 350), style=wx.TE_MULTILINE | wx.TE_READONLY)
        self.Bind(wx.EVT_BUTTON, self.send, self.buttonCheck)
        self.buttonCheck1 = wx.Button(parent=panel, label='查看在线者', pos=(50, 30))
        self.Bind(wx.EVT_BUTTON, self.lookUsers, self.buttonCheck1)
        self.buttonCheck2 = wx.Button(parent=panel, label='关闭', pos=(150, 30))
        self.Bind(wx.EVT_BUTTON, self.close, self.buttonCheck2)
        self.buttonCheck3 = wx.Button(parent=panel, label='聊天记录', pos=(50, 465))
        self.Bind(wx.EVT_BUTTON,self.talk_record,self.buttonCheck3)
        self.jindutiao = wx.Gauge(parent=panel, pos=(150, 470), size=(400, 20))
        self.listbox.AppendText("Welcome to zyy-chat-home,you could speak: \n")
        thread.start_new_thread(self.receive, ())
        self.Show()


    def talk_record(self,event):
        # conn=sqlite3.connect('用户.db')
        # c = conn.cursor()
        # c.execute("SELECT * from message1")
        # for row in c:
        #     self.listbox.AppendText("Message = \n", row[1])
        # conn.close()
        # f=open('log.txt','r')
        newwindow=RecordWindow(None)
        newwindow.Show(True)

    def send(self, event):
        # 发送消息
        input = str(self.input1.GetLineText(0)).strip()
        if input != '':
            con.write(('say ' + input + '\n').encode("utf-8"))
            self.input1.Clear()


    def lookUsers(self, event):
        # 查看当前在线用户
        def tr():
            k=0
            while k<100 :
                k=k+15
                time.sleep(0.1)
                self.jindutiao.SetValue(k)
        p=threading.Timer(0,tr,args=())
        p.start()
        con.write(b'look\n')

    def close(self, event):
        # 关闭窗口
        con.write(b'logout\n')
        con.close()
        self.Close()

    def receive(self):
        # 接受服务器的消息
        while True:
            sleep(0.6)
            result = con.read_very_eager()
            if result != '':
                self.listbox.AppendText(result)

class RecordWindow(wx.Frame):
    def __init__(self, superior):
        wx.Frame.__init__(self, parent=superior, title="聊天记录", size=(800, 800))
        panel = wx.Panel(self)
        self.Center()
        self.listbox1 = wx.TextCtrl(panel, pos=(50, 100), size=(600, 350), style=wx.TE_MULTILINE | wx.TE_READONLY)
        self.buttonCheck = wx.Button(parent=panel, label='删除', pos=(620, 680))
        self.input1 = wx.TextCtrl(parent=panel, pos=(50, 500), size=(600, 150))
        # self.Bind(wx.EVT_BUTTON, self.dele, self.buttonCheck)
        conn=sqlite3.connect("用户.db")
        c=conn.cursor()
        c.execute("select message from message2")
        # res=c.fetchone()
        for r in c :
            self.listbox1.AppendText(str(r)+"\n")
        conn.close()
    # def dele(self):
    #     tx=self.input1.GetValue()
    #     conn = sqlite3.connect("用户.db")
    #     c = conn.cursor()
    #     c.execute("delete % from message2",tx)
if __name__ == '__main__':
    app = wx.App()
    con = telnetlib.Telnet()
    frame = Mainwindow(None)
    frame.Show(True)
    app.MainLoop()