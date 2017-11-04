Set qtApp= CreateObject("QuickTest.Application")
qtApp.Launch
qtApp.Visible = true
qtApp.Open "C:\MAT_TESTS\MAT\API_MAT_Tests"
qtApp.Test.Run
qtApp.Quit
