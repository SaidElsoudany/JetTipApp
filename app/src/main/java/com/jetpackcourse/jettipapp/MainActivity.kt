package com.jetpackcourse.jettipapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jetpackcourse.jettipapp.ui.theme.JetTipAppTheme
import com.jetpackcourse.jettipapp.util.calculateTotalPerPerson
import com.jetpackcourse.jettipapp.util.calculateTotalTipAmount
import com.jetpackcourse.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetTipApp { _ ->
                AppUI()
            }
        }
    }
}

@Composable
fun AppUI() {
    var totalValuePerPerson by remember {
        mutableStateOf(0.0)
    }
    TopHeader(totalValuePerPerson)
    BillForm {
        totalValuePerPerson = it
    }
}

@Composable
fun JetTipApp(content: @Composable (innerPadding: PaddingValues) -> Unit) {
    JetTipAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column {
                content(innerPadding)
            }
        }
    }
}


@Composable
fun TopHeader(totalPerPrice: Double = 135.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(all = 20.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFE9D7F7)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                style = MaterialTheme.typography.headlineSmall,
                text = "Total Per Person",
                fontWeight = FontWeight.Bold
            )
            Text(
                style = MaterialTheme.typography.headlineLarge,
                text = "$${"%.2f".format(totalPerPrice)}",
                fontWeight = FontWeight.ExtraBold
            )
        }

    }
}


@Composable
fun BillForm(onValChange: (Double) -> Unit) {
    var numberOfPersons by remember {
        mutableIntStateOf(1)
    }
    val moneyBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(moneyBillState.value) {
        moneyBillState.value.isNotBlank()
    }
    val sliderPositionState = remember {
        mutableStateOf(0f)
    }
    val tipPercentage = remember {
        mutableStateOf(0)
    }

    var tipValue by remember {
        mutableStateOf(0.0)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                value = moneyBillState.value,
                onValueChange = { value ->
                    moneyBillState.value = value
                    onValChange(calculateTotalPerPerson(moneyBillState.value, tipPercentage.value, numberOfPersons))
                },
                label = { Text(text = "Enter Bill") },
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.AttachMoney, "Add Money")
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValChange(moneyBillState.value.toDouble())
                    keyboardController?.hide()
                }
            )
            if (validState) {
                SplitRow(numberOfPersons = numberOfPersons) {
                    numberOfPersons = it
                    onValChange(calculateTotalPerPerson(moneyBillState.value, tipPercentage.value, numberOfPersons))
                }
                TipRow(tipValue)
                SliderColumn(tipPercentage.value, sliderPositionState.value) {
                    sliderPositionState.value = it
                    tipPercentage.value = (it * 100).toInt()
                    tipValue = calculateTotalTipAmount(moneyBillState.value, tipPercentage.value)
                    onValChange(calculateTotalPerPerson(moneyBillState.value, tipPercentage.value, numberOfPersons))
                }
            } else {
                Box {}
            }
        }
    }
}


@Composable
fun SliderColumn(tipPercentage: Int = 0, value: Float = 0f, onValChange: (Float) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$tipPercentage %",
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Slider(
            value = value,
            onValueChange = onValChange,
        )
    }
}

@Composable
fun TipRow(tipValue: Double = 33.0) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Tip",
            fontWeight = FontWeight.Bold,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "$$tipValue",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SplitRow(numberOfPersons: Int, onChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Split",
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 3.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            RoundIconButton(
                imageVector = Icons.Default.Remove,
                onClick = {
                    if (numberOfPersons > 1) {
                        onChange(numberOfPersons - 1)
                    }
                })

            Text(
                modifier = Modifier.padding(10.dp),
                text = numberOfPersons.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )

            RoundIconButton(
                imageVector = Icons.Default.Add,
                onClick = {
                    onChange(numberOfPersons + 1)
                })

        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    JetTipApp {
        AppUI()
    }
}