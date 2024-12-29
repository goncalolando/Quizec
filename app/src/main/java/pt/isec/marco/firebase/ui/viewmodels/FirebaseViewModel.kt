package pt.isec.marco.firebase.ui.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch
import pt.isec.marco.firebase.utils.FAuthUtil
import pt.isec.marco.firebase.utils.FStorageUtil

data class User(
    val name: String,
    val email: String,
    val picture: String?
)
data class Pergunta(
    var id: String,
    val titulo: String,
    val imagem: String,
    val respostas: List<String>,
    val respostaCerta: List<String>,
    val tipo: String
){
    companion object {
        fun fromFirestore(document: DocumentSnapshot): Pergunta {
            return Pergunta(
                id = document.id,
                titulo = document.getString("titulo") ?: "",
                imagem = document.getString("imagem") ?: "",
                respostas = document.get("respostas") as? List<String> ?: listOf(),
                respostaCerta = document.get("respostaCerta") as? List<String> ?: listOf(),
                tipo = document.getString("tipo") ?: ""
            )
        }
    }
}
data class Questionario(var id: String, val descricao: String, val perguntas: List<String>){
    companion object {
        fun fromFirestore(document: DocumentSnapshot): Questionario {
            return Questionario(
                id = document.id,
                descricao = document.getString("descricao") ?: "",
                perguntas = document.get("perguntas") as? List<String> ?: listOf(),
            )
        }
    }
}

fun FirebaseUser.toUser(): User {
    val displayName = this.displayName?: ""
    val strEmail = this.email?: ""
    val picture = this.photoUrl?.toString()
    return User(displayName, strEmail, picture)
}

open class FirebaseViewModel : ViewModel() {
    private val _user = mutableStateOf(FAuthUtil.currentUser?.toUser())
    val perguntas = mutableStateOf(listOf<String>())
    val questionarios = mutableStateOf(listOf<String>())

    open val user : State<User?>
        get() = _user

    private val _error = mutableStateOf<String?>(null)
    open val error : State<String?>
        get() = _error

    fun createUserWithEmail(email: String, password: String) {
        if (email.isBlank() && password.isBlank())
            return;
        viewModelScope.launch {
            FAuthUtil.createUserWithEmail(email, password) { exception ->
                if (exception == null) {
                    _user.value = FAuthUtil.currentUser?.toUser()
                }
                _error.value = exception?.message
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isBlank() && password.isBlank())
            return;
        viewModelScope.launch {
            FAuthUtil.signInWithEmail(email, password) { exception ->
                if (exception == null) {
                    _user.value = FAuthUtil.currentUser?.toUser()
                }
                _error.value = exception?.message
            }
        }
    }

    fun signOut() {
        FAuthUtil.signOut()
        _user.value = null
        _error.value = null
    }


    private val _nrgames = mutableLongStateOf(0L)
    open val nrgames : State<Long>
        get() = _nrgames

    private val _topscore = mutableLongStateOf(0L)
    open val topscore : State<Long>
        get() = _topscore

    fun addDataToFirestore() {
        viewModelScope.launch {
            FStorageUtil.addDataToFirestore { exception ->
                _error.value = exception?.message
            }
        }
    }

    fun addPerguntaToFirestore(pergunta: Pergunta) {
        viewModelScope.launch {
            FStorageUtil.addPerguntaToFirestore({ exception ->
                _error.value = exception?.message
            }, pergunta, this@FirebaseViewModel)
        }
    }

    fun addQuestioanrioToFirestore(questionario: Questionario) {
        viewModelScope.launch {
            FStorageUtil.addQuestionarioToFirestore({ exception ->
                _error.value = exception?.message
            }, questionario, this@FirebaseViewModel)
        }
    }

    fun updateDataInFirestore() {
        viewModelScope.launch {
            //FirebaseUtils.updateDataInFirestore()
            FStorageUtil.updateDataInFirestoreTrans { exception ->
                _error.value = exception?.message
            }
        }
    }

    fun removeDataFromFirestore() {
        viewModelScope.launch {
            FStorageUtil.removeDataFromFirestore { exception ->
                _error.value = exception?.message
            }
        }
    }

    fun startObserver() {
        viewModelScope.launch {
            FStorageUtil.startObserver { g, t ->
                _nrgames.longValue = g
                _topscore.longValue = t
            }
        }
    }

    fun stopObserver() {
        viewModelScope.launch {
            FStorageUtil.stopObserver()
        }
    }

    private val _questionarios_criados = mutableStateOf(listOf<Questionario>())


}