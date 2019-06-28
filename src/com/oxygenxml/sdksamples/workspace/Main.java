package com.oxygenxml.sdksamples.workspace;

import com.oxygenxml.sdksamples.workspace.mainUI.XFrame;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new XFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
