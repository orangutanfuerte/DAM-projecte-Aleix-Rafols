package com.example.travellikeasigma.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import com.example.travellikeasigma.R
import com.example.travellikeasigma.ui.screens.AboutScreen
import com.example.travellikeasigma.ui.screens.AddActivityScreen
import com.example.travellikeasigma.ui.screens.EditActivityScreen
import com.example.travellikeasigma.ui.screens.EmailVerificationScreen
import com.example.travellikeasigma.ui.screens.HomeScreen
import com.example.travellikeasigma.ui.screens.ItineraryScreen
import com.example.travellikeasigma.ui.screens.CompleteProfileScreen
import com.example.travellikeasigma.ui.screens.LoginScreen
import com.example.travellikeasigma.ui.screens.RegisterScreen
import com.example.travellikeasigma.ui.screens.NewTripScreen
import com.example.travellikeasigma.ui.screens.PhotosScreen
import com.example.travellikeasigma.ui.screens.PlacesScreen
import com.example.travellikeasigma.ui.screens.PreferencesScreen
import com.example.travellikeasigma.ui.screens.EditProfileScreen
import com.example.travellikeasigma.ui.screens.ProfileScreen
import com.example.travellikeasigma.ui.screens.TermsScreen
import com.example.travellikeasigma.ui.theme.ThemeMode
import com.example.travellikeasigma.ui.viewmodels.AuthViewModel
import com.example.travellikeasigma.ui.viewmodels.ItineraryViewModel
import com.example.travellikeasigma.ui.viewmodels.PreferencesViewModel
import com.example.travellikeasigma.ui.viewmodels.ProfileViewModel
import com.example.travellikeasigma.ui.viewmodels.TripViewModel

private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(Routes.HOME)
        launchSingleTop = true
    }
}

@Composable
fun NavGraph(
    navController:        NavHostController,
    tripViewModel:        TripViewModel,
    authViewModel:        AuthViewModel,
    preferencesViewModel: PreferencesViewModel,
    snackbarHostState:    SnackbarHostState,
    onRecreate:           () -> Unit = {},
    modifier:             Modifier = Modifier
) {
    val itineraryViewModel: ItineraryViewModel = hiltViewModel()
    val scope   = rememberCoroutineScope()
    val context = LocalContext.current

    val startDestination = when {
        !authViewModel.isLoggedIn              -> Routes.LOGIN
        authViewModel.needsEmailVerification   -> Routes.EMAIL_VERIFICATION
        else                                   -> Routes.HOME
    }

    NavHost(
        navController      = navController,
        startDestination   = startDestination,
        modifier           = modifier,
        enterTransition    = { EnterTransition.None },
        exitTransition     = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition  = { ExitTransition.None }
    ) {
        composable(Routes.LOGIN) {
            LaunchedEffect(authViewModel.isLoggedIn, authViewModel.needsEmailVerification) {
                if (authViewModel.isLoggedIn) {
                    tripViewModel.reloadTrips()
                    if (authViewModel.needsEmailVerification) {
                        navController.navigate(Routes.EMAIL_VERIFICATION) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                        scope.launch {
                            snackbarHostState.showSnackbar(context.getString(R.string.snackbar_login_success))
                        }
                    }
                }
            }
            LoginScreen(
                email                = authViewModel.email,
                password             = authViewModel.password,
                isLoading            = authViewModel.isLoading,
                authError            = authViewModel.authError,
                resetPasswordSent    = authViewModel.resetPasswordSent,
                onEmailChange        = { authViewModel.email = it },
                onPasswordChange     = { authViewModel.password = it },
                onLoginClick         = { authViewModel.login(context.getString(R.string.login_error_no_local_data)) },
                onRegisterClick      = { authViewModel.authError = null; navController.navigate(Routes.REGISTER) },
                onForgotPasswordClick = { authViewModel.sendPasswordReset(context.getString(R.string.login_forgot_password_empty_email)) },
                onResetDismiss       = { authViewModel.dismissResetPasswordDialog() }
            )
        }
        composable(Routes.REGISTER) {
            LaunchedEffect(authViewModel.isLoggedIn, authViewModel.needsEmailVerification) {
                if (authViewModel.isLoggedIn && authViewModel.needsEmailVerification) {
                    tripViewModel.reloadTrips()
                    navController.navigate(Routes.EMAIL_VERIFICATION) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            }
            RegisterScreen(
                name                    = authViewModel.registerName,
                onNameChange            = { authViewModel.registerName = it },
                username                = authViewModel.registerUsername,
                onUsernameChange        = { authViewModel.registerUsername = it.filter { c -> !c.isWhitespace() } },
                dateOfBirth             = authViewModel.registerDateOfBirth,
                onDateOfBirthChange     = { authViewModel.registerDateOfBirth = it },
                email                   = authViewModel.registerEmail,
                onEmailChange           = { authViewModel.registerEmail = it },
                password                = authViewModel.registerPassword,
                onPasswordChange        = { authViewModel.registerPassword = it },
                confirmPassword         = authViewModel.registerConfirmPassword,
                onConfirmPasswordChange = { authViewModel.registerConfirmPassword = it },
                isLoading               = authViewModel.isLoading,
                authError               = authViewModel.authError,
                onRegisterClick         = { authViewModel.register(context.getString(R.string.register_error_username_taken)) },
                onLoginClick            = {
                    authViewModel.clearRegisterFields()
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.EMAIL_VERIFICATION) {
            LaunchedEffect(authViewModel.needsEmailVerification, authViewModel.isLoggedIn) {
                if (authViewModel.isLoggedIn && !authViewModel.needsEmailVerification) {
                    navController.navigate(Routes.COMPLETE_PROFILE) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            EmailVerificationScreen(
                email                = authViewModel.emailForVerification,
                onCheckVerified      = { authViewModel.checkEmailVerified() },
                onCheckVerifiedClick = { authViewModel.checkEmailVerifiedManual(context.getString(R.string.email_verification_not_verified_error)) },
                onResendEmail        = { authViewModel.sendVerificationEmail(context.getString(R.string.email_verification_too_many_requests)) },
                verificationError    = authViewModel.verificationError,
                onBackClick          = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.COMPLETE_PROFILE) {
            CompleteProfileScreen(
                phone                 = authViewModel.profilePhone,
                onPhoneChange         = { authViewModel.profilePhone = it },
                address               = authViewModel.profileAddress,
                onAddressChange       = { authViewModel.profileAddress = it },
                country               = authViewModel.profileCountry,
                onCountryChange       = { authViewModel.profileCountry = it },
                acceptsEmails         = authViewModel.profileAcceptsEmails,
                onAcceptsEmailsChange = { authViewModel.profileAcceptsEmails = it },
                isLoading             = authViewModel.isLoading,
                onSaveClick           = {
                    authViewModel.completeProfile()
                    navController.navigate(Routes.HOME) {
                        popUpTo(0) { inclusive = true }
                    }
                    scope.launch {
                        snackbarHostState.showSnackbar(context.getString(R.string.snackbar_register_success))
                    }
                }
            )
        }
        composable(Routes.HOME) {
            val homeProfileViewModel: ProfileViewModel = hiltViewModel()
            HomeScreen(
                trips             = tripViewModel.trips,
                tripIndex         = tripViewModel.selectedTripIndex,
                userName          = homeProfileViewModel.user?.name ?: "",
                onTripIndexChange = { tripViewModel.selectTrip(it) },
                onNewTripClick    = { navController.navigate(Routes.NEW_TRIP) },
                onAvatarClick     = { navController.navigate(Routes.PREFERENCES) },
                onItineraryClick  = { navController.navigateToTab(Routes.ITINERARY) },
                onPhotosClick     = { navController.navigateToTab(Routes.PHOTOS) },
                onPlacesClick     = { navController.navigateToTab(Routes.PLACES) },
                onDayClick        = { dayIndex ->
                    navController.navigateToTab(Routes.itineraryDay(dayIndex))
                },
                onDeleteTripClick = {
                    tripViewModel.deleteSelectedTrip()
                    scope.launch { snackbarHostState.showSnackbar(context.getString(R.string.snackbar_trip_deleted)) }
                }
            )
        }
        composable(
            route = "${Routes.ITINERARY}?day={day}",
            arguments = listOf(navArgument("day") { defaultValue = -1; type = NavType.IntType })
        ) { backStackEntry ->
            val initialDay = backStackEntry.arguments?.getInt("day") ?: -1
            val trip = tripViewModel.selectedTrip
            if (trip != null) {
                ItineraryScreen(
                    trip = trip,
                    initialDay = initialDay,
                    onAddActivityClick = { dayNumber ->
                        navController.navigate(Routes.addActivity(dayNumber))
                    },
                    onEditActivityClick = { dayNumber, activityId ->
                        navController.navigate(Routes.editActivity(dayNumber, activityId))
                    }
                )
            }
        }
        composable(
            route = Routes.ADD_ACTIVITY,
            arguments = listOf(navArgument("dayNumber") { type = NavType.IntType })
        ) { backStackEntry ->
            val dayNumber = backStackEntry.arguments?.getInt("dayNumber") ?: 1
            val trip = tripViewModel.selectedTrip
            if (trip != null) {
                AddActivityScreen(
                    dayNumber = dayNumber,
                    onBackClick = { navController.popBackStack() },
                    onSave = { activity ->
                        itineraryViewModel.addActivity(trip.id, dayNumber, activity)
                        tripViewModel.refreshTrips()
                        navController.popBackStack()
                        scope.launch { snackbarHostState.showSnackbar(context.getString(R.string.snackbar_activity_created)) }
                    }
                )
            }
        }
        composable(
            route = Routes.EDIT_ACTIVITY,
            arguments = listOf(
                navArgument("dayNumber") { type = NavType.IntType },
                navArgument("activityId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val dayNumber = backStackEntry.arguments?.getInt("dayNumber") ?: 1
            val activityId = backStackEntry.arguments?.getInt("activityId") ?: 0
            val trip = tripViewModel.selectedTrip
            val activity = trip?.activities?.find { it.id == activityId }

            if (trip != null && activity != null) {
                EditActivityScreen(
                    dayNumber = dayNumber,
                    activity = activity,
                    onBackClick = { navController.popBackStack() },
                    onUpdate = { updatedActivity ->
                        itineraryViewModel.updateActivity(trip.id, updatedActivity)
                        tripViewModel.refreshTrips()
                        navController.popBackStack()
                        scope.launch { snackbarHostState.showSnackbar(context.getString(R.string.snackbar_activity_updated)) }
                    },
                    onDelete = {
                        itineraryViewModel.removeActivity(trip.id, activity.id)
                        tripViewModel.refreshTrips()
                        navController.popBackStack()
                        scope.launch { snackbarHostState.showSnackbar(context.getString(R.string.snackbar_activity_deleted)) }
                    }
                )
            }
        }
        composable(Routes.PHOTOS) {
            val trip = tripViewModel.selectedTrip
            if (trip != null) {
                PhotosScreen(trip = trip)
            }
        }
        composable(Routes.PLACES) {
            val trip = tripViewModel.selectedTrip
            if (trip != null) {
                PlacesScreen(trip = trip)
            }
        }
        composable(Routes.PREFERENCES) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            PreferencesScreen(
                onBackClick            = { navController.popBackStack() },
                onProfileClick         = { navController.navigate(Routes.PROFILE) },
                onTermsClick           = { navController.navigate(Routes.TERMS) },
                onAboutClick           = { navController.navigate(Routes.ABOUT) },
                onLogoutClick          = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                    scope.launch {
                        snackbarHostState.showSnackbar(context.getString(R.string.snackbar_logout))
                    }
                },
                themeMode              = preferencesViewModel.themeMode,
                notificationsEnabled   = preferencesViewModel.notificationsEnabled,
                language               = preferencesViewModel.language,
                onThemeChange          = { preferencesViewModel.updateThemeMode(it) },
                onNotificationsChange  = { preferencesViewModel.updateNotificationsEnabled(it) },
                onLanguageChange       = { preferencesViewModel.updateLanguage(it); onRecreate() },
                userName               = profileViewModel.user?.name,
                userEmail              = profileViewModel.user?.email
            )
        }
        composable(Routes.PROFILE) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            val currentEntry by navController.currentBackStackEntryAsState()
            LaunchedEffect(currentEntry) {
                if (currentEntry?.destination?.route == Routes.PROFILE) {
                    profileViewModel.reload()
                }
            }
            ProfileScreen(
                user = profileViewModel.user,
                onBackClick = { navController.popBackStack() },
                onEditClick = { navController.navigate(Routes.EDIT_PROFILE) }
            )
        }
        composable(Routes.EDIT_PROFILE) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            EditProfileScreen(
                phone                 = profileViewModel.editPhone,
                onPhoneChange         = { profileViewModel.editPhone = it },
                address               = profileViewModel.editAddress,
                onAddressChange       = { profileViewModel.editAddress = it },
                country               = profileViewModel.editCountry,
                onCountryChange       = { profileViewModel.editCountry = it },
                acceptsEmails         = profileViewModel.editAcceptsEmails,
                onAcceptsEmailsChange = { profileViewModel.editAcceptsEmails = it },
                isSaving              = profileViewModel.isSaving,
                onSaveClick           = {
                    profileViewModel.saveEditedProfile()
                    navController.popBackStack()
                    scope.launch { snackbarHostState.showSnackbar(context.getString(R.string.snackbar_profile_updated)) }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Routes.TERMS) {
            TermsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.ABOUT) {
            AboutScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.NEW_TRIP) {
            NewTripScreen(
                onBackClick    = { navController.popBackStack() },
                onValidateName = { name -> tripViewModel.isNameAvailable(name) },
                onSave         = { name, startDate, endDate, destination, hotel, persons ->
                    tripViewModel.createTrip(name, startDate, endDate, destination, hotel, persons)
                    navController.popBackStack()
                    scope.launch { snackbarHostState.showSnackbar(context.getString(R.string.snackbar_trip_created)) }
                }
            )
        }
    }
}
