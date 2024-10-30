package com.example.appdsm.viewModel

import com.example.appdsm.roomDB.Pessoa
import com.example.appdsm.roomDB.PessoaDatabase

class Repository(private val db: PessoaDatabase) {
    suspend fun upsertPessoa(pessoa: Pessoa) {
        db.pessoaDao().upsertPessoa(pessoa)
    }
    suspend fun deletePessoa(pessoa: Pessoa) {
        db.pessoaDao().deletePessoa(pessoa)
    }

    fun getAllPessoas() =  db.pessoaDao().getAllPessoas()


}