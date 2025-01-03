package pt.isec.marco.quizec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isec.marco.quizec.ui.screens.LoginScreen
import pt.isec.marco.quizec.ui.screens.MainScreen
import pt.isec.marco.quizec.ui.theme.FirebaseTheme
import pt.isec.marco.quizec.ui.viewmodels.FirebaseViewModel


class MainActivity : ComponentActivity() {
    val viewModel : FirebaseViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            FirebaseTheme {
                Surface {
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                    ) {
                        composable("login") {
                            LoginScreen(
                                viewModel = viewModel,
                                onSuccess = {
                                    navController.navigate("main") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("main") {
                            MainScreen(
                                viewModel = viewModel,
                                onSignOut =  {
                                viewModel.signOut()
                                navController.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}


