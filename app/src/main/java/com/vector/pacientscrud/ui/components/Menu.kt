import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.vector.pacientscrud.R
import com.vector.pacientscrud.data.implementations.ApiServiceRetrofitImp
import com.vector.pacientscrud.di.RetrofitInstance
import com.vector.pacientscrud.ui.components.ViewModelExample
import com.vector.pacientscrud.ui.components.ViewModelFactory

@Composable
fun MainScreen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    // Cargar la imagen de hospital
    val hospitalImage = painterResource(id = R.drawable.hospital_image)

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Imagen de hospital en la parte superior
        Image(
            painter = hospitalImage,
            contentDescription = "Hospital Image",
            modifier = Modifier
                .fillMaxWidth() // La imagen ocupa todo el ancho
                .padding(top = 80.dp)
        )

        // Contenedor de los botones en la parte inferior
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp), // Padding alrededor de los botones
            verticalArrangement = Arrangement.Bottom, // Colocar los botones al final
            horizontalAlignment = Alignment.CenterHorizontally // Centrar horizontalmente los botones
        ) {
            Button(
                onClick = { navHostController.navigate("pacientes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp) // Espacio entre los botones
            ) {
                Text(text = "Catalogo de pacientes")
            }

            Button(
                onClick = { navHostController.navigate("alta") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Añadir Paciente")
            }
        }
    }
}
