package com.example.stushare.feature_contribution.navigation 
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.stushare.feature_contribution.ui.account.*
import com.example.stushare.feature_contribution.ui.home.HomeScreen
import com.example.stushare.feature_contribution.ui.leaderboard.LeaderboardScreen
import com.example.stushare.feature_contribution.ui.leaderboard.LeaderboardViewModel
import com.example.stushare.feature_contribution.ui.noti.NotiScreen
import com.example.stushare.feature_contribution.ui.noti.NotiViewModel
import com.example.stushare.feature_contribution.ui.search.SearchScreen
import com.example.stushare.feature_contribution.ui.upload.UploadScreen
import com.example.stushare.feature_contribution.ui.upload.UploadViewModel
import com.example.stushare.R 

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Upload : Screen("upload")
    object Noti : Screen("noti")
    object Profile : Screen("profile")
    object Leaderboard : Screen("leaderboard")
    object Settings : Screen("settings")
    object AccountSecurity : Screen("account_security")
    object PersonalInfo : Screen("personal_info")
    object ChangePassword : Screen("change_password")
    object EditPhone : Screen("edit_phone")
    object EditEmail : Screen("edit_email")
    object NotificationSettings : Screen("notification_settings")
    object AppearanceSettings : Screen("appearance_settings")
    object AboutApp : Screen("about_app")
    object ContactSupport : Screen("contact_support")
    object ReportViolation : Screen("report_violation")
    object SwitchAccount : Screen("switch_account")
    object TermsOfUse : Screen("terms_of_use")
    object PrivacyPolicy : Screen("privacy_policy")
}

@Composable
fun AppNavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Search.route) { SearchScreen() }
        composable(Screen.Upload.route) {
            val viewModel: UploadViewModel = viewModel()
            UploadScreen(viewModel = viewModel, onBackClick = { navController.popBackStack() })
        }
        composable(Screen.Noti.route) {
            val viewModel: NotiViewModel = viewModel()
            NotiScreen(viewModel = viewModel)
        }
        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = viewModel()
            ProfileScreen(
                viewModel = viewModel,
                onOpenSettings = { navController.navigate(Screen.Settings.route) },
                onOpenLeaderboard = { navController.navigate(Screen.Leaderboard.route) }
            )
        }
        composable(Screen.Leaderboard.route) {
            val viewModel: LeaderboardViewModel = viewModel()
            LeaderboardScreen(viewModel = viewModel, onBackClick = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onAccountSecurityClick = { navController.navigate(Screen.AccountSecurity.route) },
                onNotificationSettingsClick = { navController.navigate(Screen.NotificationSettings.route) },
                onAppearanceSettingsClick = { navController.navigate(Screen.AppearanceSettings.route) },
                onAboutAppClick = { navController.navigate(Screen.AboutApp.route) },
                onContactSupportClick = { navController.navigate(Screen.ContactSupport.route) },
                onReportViolationClick = { navController.navigate(Screen.ReportViolation.route) },
                onSwitchAccountClick = { navController.navigate(Screen.SwitchAccount.route) }
            )
        }
        composable(Screen.AccountSecurity.route) {
            AccountSecurityScreen(
                onBackClick = { navController.popBackStack() },
                onPersonalInfoClick = { navController.navigate(Screen.PersonalInfo.route) },
                onPhoneClick = { navController.navigate(Screen.EditPhone.route) },
                onEmailClick = { navController.navigate(Screen.EditEmail.route) },
                onPasswordClick = { navController.navigate(Screen.ChangePassword.route) },
                onDeleteAccountClick = { /* TODO */ }
            )
        }
        composable(Screen.PersonalInfo.route) { PersonalInfoScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.ChangePassword.route) { ChangePasswordScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.EditPhone.route) {
            val title = androidx.compose.ui.res.stringResource(R.string.title_edit_phone)
            val label = androidx.compose.ui.res.stringResource(R.string.label_edit_phone)
            EditAttributeScreen(title = title, label = label, initialValue = "0123456789", onBackClick = { navController.popBackStack() })
        }
        composable(Screen.EditEmail.route) {
            val title = androidx.compose.ui.res.stringResource(R.string.title_edit_email)
            val label = androidx.compose.ui.res.stringResource(R.string.label_edit_email)
            EditAttributeScreen(title = title, label = label, initialValue = "dungdao@test.com", onBackClick = { navController.popBackStack() })
        }
        composable(Screen.NotificationSettings.route) { NotificationSettingsScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.AppearanceSettings.route) { AppearanceSettingsScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.AboutApp.route) {
            AboutAppScreen(
                onBackClick = { navController.popBackStack() },
                onTermsClick = { navController.navigate(Screen.TermsOfUse.route) },
                onPrivacyClick = { navController.navigate(Screen.PrivacyPolicy.route) }
            )
        }
        composable(Screen.TermsOfUse.route) { TermsOfUseScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.PrivacyPolicy.route) { PrivacyPolicyScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.ContactSupport.route) { ContactSupportScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.ReportViolation.route) { ReportViolationScreen(onBackClick = { navController.popBackStack() }) }
        composable(Screen.SwitchAccount.route) { SwitchAccountScreen(onBackClick = { navController.popBackStack() }) }
    }
}