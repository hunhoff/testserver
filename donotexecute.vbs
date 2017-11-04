Set qtApp= CreateObject("QuickTest.Application")
qtApp.Launch
qtApp.Visible = false
qtApp.Open "C:\MAT_TESTS\MAT\DoNotExecute"
qtApp.Test.Run
qtApp.Quit
