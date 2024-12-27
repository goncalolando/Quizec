package pt.isec.marco.firebase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import pt.isec.marco.firebase.ui.viewmodels.FirebaseViewModel
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.marco.firebase.ui.viewmodels.Pergunta
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun TipoPerguntaCard(
    pergunta: Pergunta,
//    onSelectContact: (Pergunta) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(255,224,192)
        )
    ) {
        Text(pergunta.titulo)
        pergunta.respostas.forEach {
            Text(it)
        }
        Text(pergunta.respostaCerta.toString())
    }
}


@Composable
fun CriarPerguntaScreen(
    viewModel: FirebaseViewModel,
    navController: NavHostController
) {
    val tiposPerguntas = listOf(
        Pergunta(
            "identificador", "TItulo",
            "img", listOf("Resposta 3", "Resposta 4"), listOf("Resposta 3")
        ),
        Pergunta(
            "identificador", "TItulo",
            "img", listOf("Resposta 3", "Resposta 4"), listOf("Resposta 3")
        ),
        Pergunta(
            "identificador", "TItulo",
            "img", listOf("Resposta 3", "Resposta 4"), listOf("Resposta 3")
        )
    )
    Box(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(pageCount = {
            3
        })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val pergunta = tiposPerguntas[page]
            TipoPerguntaCard(
                pergunta = pergunta
            )
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(16.dp)
                    )
                }
            }
        }
    }
}

