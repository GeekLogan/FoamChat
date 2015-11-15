//
//  AppDelegate.swift
//  FoamChat
//
//  Created by Logan Walker on 11/15/15.
//  Copyright (c) 2015 FoamChatDevelopers. All rights reserved.
//

import Cocoa

@NSApplicationMain
class AppDelegate: NSObject, NSApplicationDelegate {

    var Users = [String]()
    var Messages = [String]()
    var MessageSenders = [String]()
    
    func applicationDidFinishLaunching(aNotification: NSNotification) {
        var proc = ProcessInterface()
        
    }

    func applicationWillTerminate(aNotification: NSNotification) {
        // Insert code here to tear down your application
    }


}

