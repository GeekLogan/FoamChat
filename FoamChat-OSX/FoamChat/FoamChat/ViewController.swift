//
//  ViewController.swift
//  FoamChat
//
//  Created by Logan Walker on 11/15/15.
//  Copyright (c) 2015 FoamChatDevelopers. All rights reserved.
//

import Cocoa

class ViewController: NSViewController, NSTableViewDataSource, NSTableViewDelegate  {
    
    @IBOutlet weak var RightTable: NSTableView!
    @IBOutlet weak var LeftTable: NSTableView!

    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        RightTable.setDataSource( self )
        LeftTable.setDataSource( self )
    }

    override var representedObject: AnyObject? {
        didSet {
            // Update the view, if already loaded.
        }
    }
    
    func numberOfRowsInTableView(tableView: NSTableView) -> Int {
        return 10
    }
    
    func tableView(aTableView: NSTableView, objectValueForTableColumn aTableColumn: NSTableColumn?,row rowIndex: Int) -> AnyObject? {
        
        if aTableColumn != nil {
            return "Test"
        } else {
            return "Test"
        }
    }
}

