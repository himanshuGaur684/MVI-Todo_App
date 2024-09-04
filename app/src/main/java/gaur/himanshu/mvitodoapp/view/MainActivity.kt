package gaur.himanshu.mvitodoapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import gaur.himanshu.mvitodoapp.intent.TodoIntent
import gaur.himanshu.mvitodoapp.model.local.Todo
import gaur.himanshu.mvitodoapp.model.repository.TodoRepository
import gaur.himanshu.mvitodoapp.view.ui.theme.MVITodoAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: TodoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVITodoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        val list by repository.getAllTodoList()
                            .collectAsState(initial = emptyList())
                        val scope = rememberCoroutineScope()
                        MainScreen(list = list) { intent ->
                            when (intent) {
                                is TodoIntent.Delete -> scope.launch(Dispatchers.IO) {
                                    repository.delete(intent.todo)
                                }

                                is TodoIntent.Insert -> scope.launch(Dispatchers.IO)  {
                                    repository.insert(intent.todo)
                                }

                                is TodoIntent.Update -> scope.launch(Dispatchers.IO)  {
                                    repository.update(intent.todo)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(list: List<Todo>, onIntent: (TodoIntent) -> Unit) {

    val title = remember {
        mutableStateOf("")
    }

    Scaffold(topBar = { TopAppBar(title = { Text(text = "Todo Items") }) }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (list.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(text = "Nothing found")
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(list) {
                        val isChecked = remember {
                            mutableStateOf(it.isDone)
                        }
                        Column(
                            modifier = Modifier
                                .combinedClickable(enabled = true,
                                    onClick = {},
                                    onLongClick = {
                                        onIntent.invoke(TodoIntent.Delete(it))
                                    })
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = it.title)
                                Checkbox(checked = isChecked.value, onCheckedChange = { check ->
                                    isChecked.value = check
                                    onIntent.invoke(TodoIntent.Update(it.copy(isDone = check)))
                                })
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextField(value = title.value, onValueChange = {
                    title.value = it
                }, modifier = Modifier.fillMaxWidth())
                Button(onClick = {
                    onIntent.invoke(
                        TodoIntent.Insert(
                            Todo(title = title.value, isDone = false, id = 0)
                        )
                    )
                    title.value = ""
                }) {
                    Text(text = "Save todo")
                }
            }
        }
    }

}





