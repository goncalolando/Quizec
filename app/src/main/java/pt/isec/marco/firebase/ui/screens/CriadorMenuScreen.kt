package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel

@Composable
fun CriadorMenuScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController
) {
    Column() {
        Button(
            onClick = {
                navController.navigate("menu-criador") {
                    popUpTo("historico-questionarios") {
                        inclusive = true
                    }
                }
            }
        ) {
            Text("Historico questionarios")
        }
        Button(
            onClick = {
                navController.navigate("criar-questionario") {
                popUpTo("criar-questionario") {
                    inclusive = true
                }
            }}
        ) {
            Text("Criar questionario")
        }
    }
}