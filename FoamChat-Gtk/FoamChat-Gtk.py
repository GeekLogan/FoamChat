from gi.repository import Gtk, GObject
from time import sleep

class FoamChat(Gtk.Window):

    def sendCallBack(self, widget, data=None):
        print data.get_text()

    def __init__(self):
        Gtk.Window.__init__(self, title="FoamChat")
        builder = Gtk.Builder()
        builder.add_from_file("UIBase.glade")
        pane = Gtk.Paned.new(Gtk.Orientation.HORIZONTAL)
        userGrid.set_row_spacing(2)
        userGrid.set_orientation(Gtk.Orientation.VERTICAL)
        viewGrid = builder.get_object("viewGrid")
        for x in range(0, 10):
            buffer = Gtk.TextBuffer()
            userBuilder = Gtk.Builder()
            userBuilder.add_from_file("gridItems.glade")
            user = userBuilder.get_object("user")
            buffer.set_text("User" + str(x))
            user.set_buffer(buffer)
            userGrid.add(user)
        pane.add1(userGrid)
        messageScroll = viewGrid.get_child_at(0, 0)
        send = viewGrid.get_child_at(1, 1)
        inputText = viewGrid.get_child_at(0, 1)
        send.connect("clicked",self.sendCallBack, inputText)
        messageGrid.set_row_spacing(2)
        messageGrid.set_orientation(Gtk.Orientation.VERTICAL)
        for x in range(0, 10):
            buffer = Gtk.TextBuffer()
            messageBuilder = Gtk.Builder()
            messageBuilder.add_from_file("gridItems.glade")
            message = messageBuilder.get_object("message")
            buffer.set_text("This is message number " + str(x))
            message.set_buffer(buffer)
            messageGrid.add(message)
        messageScroll.add(messageGrid)
        pane.add2(viewGrid)
        self.add(pane)
        self.show_all()

userGrid = Gtk.Grid()
messageGrid = Gtk.Grid()


win = FoamChat()
win.connect("delete-event", Gtk.main_quit)
Gtk.main()
