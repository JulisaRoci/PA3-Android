package com.isil.android.pa3.presentation.screens.gestion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Sell
import androidx.compose.material.icons.rounded.Wc
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.isil.android.pa3.domain.model.TipoCatalogo
import com.isil.android.pa3.ui.theme.IOSColors

enum class Modulo(
    val titulo: String,
    val descripcion: String,
    val icon: ImageVector,
    val color: Color,
    val catalogo: TipoCatalogo? = null
) {
    SERVICIOS("Servicios", "Servicios del taller y sus precios", Icons.Rounded.Build, IOSColors.Orange),
    VEHICULOS("Vehículos", "Placa, modelo, marca y dueño", Icons.Rounded.DirectionsCar, IOSColors.Blue),
    CLIENTES("Clientes", "Datos personales y de contacto", Icons.Rounded.Groups, IOSColors.Green),
    EMPLEADOS("Empleados", "Usuarios y contraseñas de acceso", Icons.Rounded.Badge, IOSColors.Indigo),
    TIPO_DOCUMENTO(TipoCatalogo.TIPO_DOCUMENTO.titulo, "", Icons.Rounded.CreditCard, IOSColors.Teal, TipoCatalogo.TIPO_DOCUMENTO),
    SEXO(TipoCatalogo.SEXO.titulo, "", Icons.Rounded.Wc, IOSColors.Pink, TipoCatalogo.SEXO),
    ROL(TipoCatalogo.ROL.titulo, "", Icons.Rounded.AdminPanelSettings, IOSColors.Purple, TipoCatalogo.ROL),
    MARCA(TipoCatalogo.MARCA.titulo, "", Icons.Rounded.Sell, IOSColors.Red, TipoCatalogo.MARCA),
    ESTADO_CIVIL(TipoCatalogo.ESTADO_CIVIL.titulo, "", Icons.Rounded.Favorite, IOSColors.Pink, TipoCatalogo.ESTADO_CIVIL),
    DISTRITO(TipoCatalogo.DISTRITO.titulo, "", Icons.Rounded.Place, IOSColors.Green, TipoCatalogo.DISTRITO),
    COLOR(TipoCatalogo.COLOR.titulo, "", Icons.Rounded.Palette, IOSColors.Cyan, TipoCatalogo.COLOR);

    companion object {
        val principales = listOf(SERVICIOS, VEHICULOS, CLIENTES, EMPLEADOS)
        val catalogos = entries.filter { it.catalogo != null }
    }
}
