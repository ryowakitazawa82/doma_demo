package com.example.doma.demodemo.entity

import org.seasar.doma.Entity
import org.seasar.doma.Id

@Entity(immutable = true)
data class DemoRecord (@Id val id: Int,val name: String)