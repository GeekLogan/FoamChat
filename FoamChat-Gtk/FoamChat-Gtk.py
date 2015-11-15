from gi.repository import Gtk, GObject
from subprocess import Popen, PIPE
from time import sleep
import json
import threading

userIds = []
kernel = Popen(["java", "-jar", "FoamChatKernel.jar"], stdin=PIPE, stdout=PIPE)

class FoamChat(Gtk.Window):
    def sendCallBack(self, widget, data=None):
        for userId in userIds:
            kernel.stdin.write('msg:'+str(userId)+':'+data.get_text());
        data.set_text('')

    def __init__(self):
        Gtk.Window.__init__(self, title="FoamChat")
        builder = Gtk.Builder()
        builder.add_from_file("UIBase.glade")
        pane = Gtk.Paned.new(Gtk.Orientation.HORIZONTAL)
        viewGrid = builder.get_object("viewGrid")
        global userGrid
        userGrid = Gtk.Grid()
        global userSize
        userSize = 0
        pane.add1(userGrid)
        messageScroll = viewGrid.get_child_at(0, 0)
        send = viewGrid.get_child_at(1, 1)
        inputText = viewGrid.get_child_at(0, 1)
        send.connect("clicked", self.sendCallBack, inputText)
        global messageGrid
        messageGrid = Gtk.Grid()
        messageScroll.add(messageGrid)
        pane.add2(viewGrid)
        connect()
        update()
        thread = Update()
        thread.start()
        self.add(pane)
        self.show_all()
        GObject.timeout_add(1000, updateGui)


def connect():
    kernel.stdin.write('/home/chris/testId\n')
    kernel.stdin.write('Chris\n')
    kernel.stdin.write('25.16.95.241\n')
    kernel.stdin.write('Y\n')
    sleep(1)


def quitProgram(one,two):
    kernel.stdin.write('exit\n')
    Gtk.main_quit()


def update():
    kernel.stdin.write('lsu\n')
    sleep(1)
    raw = kernel.stdout.readline()
    print raw
    global users
    users = json.loads(raw)
    kernel.stdin.write('lsm\n')
    sleep(1)
    raw = kernel.stdout.readline();
    print raw
    global messages
    messages = json.loads(raw)


class Update(threading.Thread):

    def __init__(self):
        super(Update, self).__init__()

    def run(self):
        while True:
            update()


def updateGui():
    userGrid.set_row_spacing(2)
    userGrid.set_orientation(Gtk.Orientation.VERTICAL)
    size = len(users)
    for i in range(len(userGrid.get_children()), size):
        user = users[i]
        buffer = Gtk.TextBuffer()
        userBuilder = Gtk.Builder()
        userBuilder.add_from_file("gridItems.glade")
        userItem = userBuilder.get_object("user")
        buffer.set_text(user['displayName'])
        userIds.append(user['id'])
        userItem.set_buffer(buffer)
        userGrid.add(userItem)
    messageGrid.set_row_spacing(2)
    messageGrid.set_orientation(Gtk.Orientation.VERTICAL)
    size = len(messages)
    for i in range(len(messageGrid.get_children()), size):
        message = messages[i]
        buffer = Gtk.TextBuffer()
        msgBuilder = Gtk.Builder()
        msgBuilder.add_from_file("gridItems.glade")
        msg = msgBuilder.get_object("message")
        name = message['name']
        buffer.set_text(name + ": " +message['text'])
        msg.set_buffer(buffer)
        messageGrid.add(msg)
    return True

win = FoamChat()
win.connect("delete-event", quitProgram)
Gtk.main()

