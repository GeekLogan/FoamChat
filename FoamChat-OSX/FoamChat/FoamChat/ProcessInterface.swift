//
//  ProcessInterface.swift
//  FoamChat
//
//  Created by Logan Walker on 11/15/15.
//  Copyright (c) 2015 FoamChatDevelopers. All rights reserved.
//

import Foundation; import Cocoa

class ProcessInterface {
    var Users = [String]()
    var Messages = [String]()
    var MessageSenders = [String]()
    
    func readToChar( file: NSFileHandle?, end: NSString ) -> String {
        var out : String = ""
        var byte = file!.readDataOfLength(1)
        var i = 0
        while(NSString(data: byte, encoding: NSUTF8StringEncoding) != end && i < 100) {
            out += NSString(data: byte, encoding: NSUTF8StringEncoding) as! String
            byte = file!.readDataOfLength(1)
            i++
        }
        return out
    }
    
    init() {
        var task = NSTask()
        task.launchPath = "/usr/bin/java"
        task.arguments = ["-jar", "/Users/logan/Downloads/FoamChatKernel.jar"]
        
        let pipe = NSPipe()
        task.standardOutput = pipe
        
        let inpipe = NSPipe()
        task.standardInput = inpipe;
        var toWrite = ("/Users/logan/fckey\nLogan\n25.16.95.241\nlsu\nlsu\nlsm\nlsma\nexit\n").dataUsingEncoding(NSUTF8StringEncoding)!
        task.standardInput.fileHandleForWriting.writeData( toWrite )
        task.launch()
        
        print(NSString(data: task.standardOutput.fileHandleForReading.readDataToEndOfFile(), encoding:NSUTF8StringEncoding))
        
        /*
        while(true) {
            sleep(1000)
            //var out = readToChar(task.standardError.fileHandleForReading, end: "\n")
            //print(out)
            toWrite = ("lsu\n").dataUsingEncoding(NSUTF8StringEncoding)!
            task.standardInput.fileHandleForWriting.writeData( toWrite )
            sleep(500)
        }
*/
        

    }
}
