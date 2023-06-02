/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Native
 *  com.sun.jna.platform.win32.User32
 *  com.sun.jna.platform.win32.WinDef$DWORD
 *  com.sun.jna.platform.win32.WinDef$HWND
 *  com.sun.jna.platform.win32.WinDef$WORD
 *  com.sun.jna.platform.win32.WinUser$INPUT
 */
package net.runelite.client.util;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import java.awt.Component;
import java.awt.Frame;

public class WinUtil {
    public static void requestForeground(Frame frame) {
        frame.setState(0);
        User32 user32 = User32.INSTANCE;
        WinUser.INPUT input = new WinUser.INPUT();
        input.type = new WinDef.DWORD(1L);
        input.input.ki.wVk = new WinDef.WORD(133L);
        user32.SendInput(new WinDef.DWORD(1L), (WinUser.INPUT[])input.toArray(1), input.size());
        WinDef.HWND hwnd = new WinDef.HWND(Native.getComponentPointer((Component)frame));
        user32.SetForegroundWindow(hwnd);
    }
}

