package mz.delcio.secondspace

import android.content.Context
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Starter PIN storage: salted PBKDF2 hash in app-private preferences.
 * Production hardening should add retry backoff, secure-screen flags,
 * encrypted private-file storage, and a reviewed threat model.
 */
class PinStore(context: Context) {
    private val prefs = context.getSharedPreferences("private_pin_store", Context.MODE_PRIVATE)
    private val random = SecureRandom()

    fun hasPin(): Boolean = prefs.contains("pin_hash") && prefs.contains("pin_salt")

    fun savePin(pin: String) {
        val salt = ByteArray(16).also { random.nextBytes(it) }
        val hash = derive(pin, salt)
        prefs.edit()
            .putString("pin_salt", Base64.encodeToString(salt, Base64.NO_WRAP))
            .putString("pin_hash", Base64.encodeToString(hash, Base64.NO_WRAP))
            .apply()
    }

    fun verifyPin(pin: String): Boolean {
        val saltText = prefs.getString("pin_salt", null) ?: return false
        val hashText = prefs.getString("pin_hash", null) ?: return false
        val salt = Base64.decode(saltText, Base64.NO_WRAP)
        val expected = Base64.decode(hashText, Base64.NO_WRAP)
        val actual = derive(pin, salt)
        return MessageDigest.isEqual(expected, actual)
    }

    private fun derive(pin: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(pin.toCharArray(), salt, 120_000, 256)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }
}
