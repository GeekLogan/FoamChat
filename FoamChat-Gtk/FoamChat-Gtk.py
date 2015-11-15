from gi.repository import Gtk, GObject
from subprocess import Popen, PIPE
from time import sleep
import json



class FoamChat(Gtk.Window):
    def sendCallBack(self, widget, data=None):
        for id in userIds:
            kernel.stdin.write('msg:'+str(id)+':'+data.get_text());
        data.set_text('')

    def __init__(self):
        Gtk.Window.__init__(self, title="FoamChat")
        builder = Gtk.Builder()
        builder.add_from_file("UIBase.glade")
        pane = Gtk.Paned.new(Gtk.Orientation.HORIZONTAL)
        viewGrid = builder.get_object("viewGrid")
        pane.add1(userGrid)
        messageScroll = viewGrid.get_child_at(0, 0)
        send = viewGrid.get_child_at(1, 1)
        inputText = viewGrid.get_child_at(0, 1)
        send.connect("clicked", self.sendCallBack, inputText)
        messageScroll.add(messageGrid)
        pane.add2(viewGrid)
        self.add(pane)
        connect()
        self.show_all()


userGrid = Gtk.Grid()
users = None
messages = None
userIds = []
messageGrid = Gtk.Grid()
kernel = Popen(["java", "-jar", "FoamChatKernel.jar"], stdin=PIPE, stdout=PIPE)



def connect():
    kernel.stdin.write('/home/chris/testId\n')
    kernel.stdin.write('Chris\n')
    kernel.stdin.write('25.6.157.8\n')
    kernel.stdin.write('Y\n')
    sleep(2)

def quit():
    kernel.stdin.write('exit')
    Gtk.main_quit()
def update():
    kernel.stdin.write('lsu\n')
    raw = kernel.stdout.readline()
    print raw
    users = json.loads(raw)
    kernel.stdin.write('lsm\n')
    sleep(2)
    raw = kernel.stdout.readline();
    print raw
    messages = json.loads(raw)
def updateGui():

    userGrid.set_row_spacing(2)
    userGrid.set_orientation(Gtk.Orientation.VERTICAL)
    for user in users:
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

    for message in messages:
        buffer = Gtk.TextBuffer()
        msgBuilder = Gtk.Builder()
        msgBuilder.add_from_file("gridItems.glade")
        msg = msgBuilder.get_object("message")
        kernel.stdin.write('getn-'+message['from'])
        name = kernel.stdout.readline()
        buffer.set_text(name + ": " +message['text'])
        msg.set_buffer(buffer)
        messageGrid.add(msg)
    return True

win = FoamChat()
win.connect("delete-event", quit)
GObject.timeout_add(1000, update)
Gtk.main()

