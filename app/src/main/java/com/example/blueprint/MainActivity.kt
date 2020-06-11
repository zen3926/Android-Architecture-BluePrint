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
import com.google.android.material.snackbar.Snackbar

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
        setSupportActionBar(binding.appBar.toolbar)
        checkIntent(intent)

        binding.appBar.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntent(intent)
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
}
