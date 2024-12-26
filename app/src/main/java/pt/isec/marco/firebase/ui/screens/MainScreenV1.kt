package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel

@Composable
fun QuizecScreen(
    modifier: Modifier = Modifier,
    viewModel: FirebaseViewModel,
    navController: NavHostController = rememberNavController()
){
    val error by remember { viewModel.error }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (error != null) {
            Text(text = "Error: $error", Modifier.background(Color(255, 0, 0)))
            Spacer(modifier = Modifier.height(16.dp))
        }
        Text("Quizec")
        Text("User: ${viewModel.user.value?.email ?: ""}")
        Text("IMAGEM DO QUIZEC")
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("firestore") {
                    popUpTo("firestore") {
                        inclusive = true
                    }
                }
            }) {
            Text("Criador")
        }
        Button(
            onClick = {  }) {
            Text("Utilizador")
        }

    }

}
