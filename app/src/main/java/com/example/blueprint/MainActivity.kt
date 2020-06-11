package com.example.blueprint

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.blueprint.databinding.ActivityMainBinding
import com.example.blueprint.repository.UserManager
import com.example.blueprint.widget.MyWidgetProvider
import com.example.blueprint.widget.MyWidgetService.Companion.KEY_WIDGET_ACTION
import com.example.blueprint.widget.MyWidgetService.Companion.REQUEST_READ

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    init {
        lifecycleScope.launchWhenCreated {
            startWidgetObservers()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkIntent(intent)
        initNavComponents()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
                || handleOtherAction(item)
                || super.onOptionsItemSelected(item)

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntent(intent)
    }

    private fun initNavComponents() {
        setSupportActionBar(binding.appBar.toolbar)
        binding.appBar.fab.setOnClickListener {
            startShare()
        }
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_welcome, R.id.nav_read, R.id.loginFragment, R.id.nav_home),
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    private fun handleOtherAction(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_share -> {
            startShare()
            true
        }
        else -> false
    }

    private fun checkIntent(intent: Intent?) {
        intent?.run {
            val isFromShortcut = intent.action == Intent.ACTION_RUN
                    || intent.extras?.getInt(KEY_WIDGET_ACTION) == REQUEST_READ
            if (isFromShortcut) findNavController(R.id.nav_host_fragment).navigate(R.id.nav_read)
        }
    }

    private fun startWidgetObservers() {
        UserManager.getRepo(this).user.observe(this, Observer {
            MyWidgetProvider.updateWidget(this)
        })
    }

    private fun startShare() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, "gzn3926@gmail.com")
            putExtra(Intent.EXTRA_SUBJECT, "Android Architecture BluePrint")
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out GitHub Repo @ https://github.com/zen3926/Android-Architecture-BluePrint.git"
            )
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}
