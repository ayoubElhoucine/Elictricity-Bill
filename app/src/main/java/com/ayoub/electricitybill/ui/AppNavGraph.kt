package com.ayoub.electricitybill.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ayoub.electricitybill.model.Bill
import com.ayoub.electricitybill.ui.bill.details.BillDetailsScreen
import com.ayoub.electricitybill.ui.bill.draft.DraftBillScreen
import com.ayoub.electricitybill.ui.bill.newBill.NewBillScreen
import com.ayoub.electricitybill.ui.home.HomeScreen
import com.ayoub.electricitybill.ui.login.LoginScreen
import com.ayoub.electricitybill.ui.splash.SplashScreen

object Screens {
    const val SPLASH_SCREEN = "splash_screen"
    const val HOME = "home"
    const val LOGIN = "login"
    const val NEW_BILL = "new_bill"
    const val DRAFT_BILL = "draft_bill"
    const val BILL_DETAILS = "bill_details"
}

@Composable
fun AppNavGraph(
    finishActivity: () -> Unit = {},
    appState: AppState,
    startDestination: String = Screens.SPLASH_SCREEN,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Screens.SPLASH_SCREEN) {
            BackHandler(onBack = appState::popBack)
            SplashScreen(
                onHome = {
                    navController.navigate(Screens.HOME)
                },
                onLogin = {
                    navController.navigate(Screens.LOGIN)
                }
            )
        }
        composable(Screens.LOGIN) {
            BackHandler(onBack = appState::popBack)
            LoginScreen {
                navController.navigate(Screens.HOME)
            }
        }
        composable(Screens.HOME) {
            BackHandler(onBack = appState::popBack)
            HomeScreen(
                onNewBill = { appState.navigateTo(Screens.NEW_BILL) },
                onDraftedBill = { appState.navigateTo(Screens.DRAFT_BILL) },
                onBillDetails = {
                    appState.navController.currentBackStackEntry?.savedStateHandle?.set(key = "bill", value = it)
                    appState.navigateTo(Screens.BILL_DETAILS)
                },
            )
        }
        composable(Screens.NEW_BILL) {
            BackHandler(onBack = appState::popBack)
            NewBillScreen(
                onBack = { appState.popBack(to = Screens.HOME) },
            )
        }
        composable(Screens.DRAFT_BILL) {
            BackHandler(onBack = appState::popBack)
            DraftBillScreen(
                onBack = appState::popBack,
            )
        }
        composable(Screens.BILL_DETAILS) {
            BackHandler(onBack = appState::popBack)
            appState.navController.previousBackStackEntry?.savedStateHandle?.get<Bill>("bill")
                ?.let { bill ->
                    BillDetailsScreen(
                        onBack = appState::popBack,
                        bill = bill,
                    )
                }
        }
    }
}