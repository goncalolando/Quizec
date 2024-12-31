package pt.isec.marco.quizec.ui.viewmodels

import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUser(): Utilizador {
    val displayName = this.displayName?: ""
    val strEmail = this.email?: ""
    val picture = this.photoUrl?.toString()
    return Utilizador(displayName, strEmail, picture)
}