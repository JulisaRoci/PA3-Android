package com.isil.android.pa3.data.datasource.local

import android.util.Base64
import com.isil.android.pa3.data.model.EmpleadoDto
import org.json.JSONObject
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object TokenGenerator {
    private const val SECRET = "rapidosyfuriosos-pa3"
    private const val EXPIRATION_SECONDS = 60 * 60
    private const val FLAGS = Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING

    fun generate(empleado: EmpleadoDto): String {
        val now = System.currentTimeMillis() / 1000
        val header = JSONObject()
            .put("alg", "HS256")
            .put("typ", "JWT")
        val payload = JSONObject()
            .put("sub", empleado.usuario)
            .put("codigo", empleado.codigo)
            .put("rol", empleado.rol?.nombre)
            .put("jti", UUID.randomUUID().toString())
            .put("iat", now)
            .put("exp", now + EXPIRATION_SECONDS)

        val unsigned = "${encode(header.toString().toByteArray())}.${encode(payload.toString().toByteArray())}"
        return "$unsigned.${encode(sign(unsigned))}"
    }

    private fun sign(data: String): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(SECRET.toByteArray(), "HmacSHA256"))
        return mac.doFinal(data.toByteArray())
    }

    private fun encode(bytes: ByteArray): String = Base64.encodeToString(bytes, FLAGS)
}
