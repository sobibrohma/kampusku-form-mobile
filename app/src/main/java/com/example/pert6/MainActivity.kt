package com.example.pert6


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- Palet biru ----------
private val BlueDeep = Color(0xFF0D47A1)
private val BlueMain = Color(0xFF1976D2)
private val BlueSky = Color(0xFF42A5F5)
private val BlueMist = Color(0xFFE3F2FD)
private val InkDark = Color(0xFF0B1F3A)
private val InkSoft = Color(0xFF5C6F8A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen() {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var sandi by remember { mutableStateOf("") }
    var konfirmasi by remember { mutableStateOf("") }
    var lihatSandi by remember { mutableStateOf(false) }
    var lihatKonfirmasi by remember { mutableStateOf(false) }
    var setuju by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }

    val emailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val sandiValid = sandi.length >= 8
    val cocok = sandi == konfirmasi && konfirmasi.isNotEmpty()
    val formValid = nama.isNotBlank() && emailValid && sandiValid && cocok && setuju

    // kekuatan sandi 0..4
    val kekuatan = listOf(
        sandi.length >= 8,
        sandi.any { it.isDigit() },
        sandi.any { it.isUpperCase() },
        sandi.any { !it.isLetterOrDigit() }
    ).count { it }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BlueDeep, BlueMain, BlueSky)))
    ) {
        // dekorasi lingkaran transparan
        Box(
            Modifier
                .size(220.dp)
                .offset(x = (-70).dp, y = (-60).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
        )
        Box(
            Modifier
                .size(160.dp)
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = 90.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.10f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 22.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // ---------- Header ----------
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.School, null, tint = BlueMain)
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    "KampusKu",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "Buat akun baru",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Generasi Pencerah",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            // ---------- Kartu formulir ----------
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(28.dp), spotColor = BlueDeep)
                    .animateContentSize(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    BlueField(
                        value = nama,
                        onChange = { nama = it },
                        label = "Nama lengkap",
                        icon = Icons.Default.Person,
                        error = if (submitted && nama.isBlank()) "Nama wajib diisi" else null
                    )
                    BlueField(
                        value = email,
                        onChange = { email = it },
                        label = "Email",
                        icon = Icons.Default.Email,
                        keyboard = KeyboardType.Email,
                        error = if ((submitted || email.isNotEmpty()) && !emailValid)
                            "Format email belum benar" else null
                    )
                    BlueField(
                        value = sandi,
                        onChange = { sandi = it },
                        label = "Kata sandi",
                        icon = Icons.Default.Lock,
                        keyboard = KeyboardType.Password,
                        isPassword = true,
                        visible = lihatSandi,
                        onToggle = { lihatSandi = !lihatSandi },
                        error = if (submitted && !sandiValid) "Minimal 8 karakter" else null
                    )

                    // indikator kekuatan sandi
                    if (sandi.isNotEmpty()) {
                        val warna by animateColorAsState(
                            when (kekuatan) {
                                0, 1 -> Color(0xFFE57373)
                                2 -> Color(0xFFFFB74D)
                                3 -> BlueSky
                                else -> Color(0xFF26A69A)
                            }, label = "kekuatan"
                        )
                        val teks = listOf("Sangat lemah", "Lemah", "Cukup", "Kuat", "Sangat kuat")[kekuatan]
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                repeat(4) { i ->
                                    Box(
                                        Modifier
                                            .weight(1f)
                                            .height(5.dp)
                                            .clip(CircleShape)
                                            .background(if (i < kekuatan) warna else BlueMist)
                                    )
                                }
                            }
                            Text(
                                "Kekuatan sandi: $teks",
                                color = warna,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    BlueField(
                        value = konfirmasi,
                        onChange = { konfirmasi = it },
                        label = "Konfirmasi kata sandi",
                        icon = Icons.Default.Lock,
                        keyboard = KeyboardType.Password,
                        isPassword = true,
                        visible = lihatKonfirmasi,
                        onToggle = { lihatKonfirmasi = !lihatKonfirmasi },
                        error = if (konfirmasi.isNotEmpty() && !cocok) "Kata sandi tidak sama" else null
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = setuju,
                            onCheckedChange = { setuju = it },
                            colors = CheckboxDefaults.colors(checkedColor = BlueMain)
                        )
                        Text(
                            "Saya menyetujui ketentuan penggunaan",
                            color = InkSoft,
                            fontSize = 13.sp
                        )
                    }
                    if (submitted && !setuju) {
                        Text(
                            "Centang persetujuan terlebih dahulu",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }

                    // tombol gradien
                    Button(
                        onClick = { submitted = true /* TODO: panggil proses pendaftaran */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        if (formValid) listOf(BlueDeep, BlueSky)
                                        else listOf(Color(0xFF90A4C0), Color(0xFFB0C4DE))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Daftar sekarang",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // ---------- Footer ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Sudah punya akun? ", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
                Text(
                    "Masuk",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

// ---------- Komponen field yang dipakai ulang ----------
@Composable
private fun BlueField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboard: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    visible: Boolean = false,
    onToggle: () -> Unit = {},
    error: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        isError = error != null,
        supportingText = { if (error != null) Text(error) },
        shape = RoundedCornerShape(16.dp),
        leadingIcon = { Icon(icon, null) },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = onToggle) {
                    Icon(
                        if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Tampilkan sandi"
                    )
                }
            }
        },
        visualTransformation = if (isPassword && !visible) PasswordVisualTransformation()
        else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BlueMain,
            unfocusedBorderColor = Color(0xFFCFE0F5),
            focusedLabelColor = BlueMain,
            focusedLeadingIconColor = BlueMain,
            unfocusedLeadingIconColor = InkSoft,
            focusedContainerColor = BlueMist.copy(alpha = 0.4f),
            unfocusedContainerColor = Color.White,
            cursorColor = BlueMain,
            focusedTextColor = InkDark,
            unfocusedTextColor = InkDark
        )
    )
}