package com.cat.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cat.view.R
import com.cat.view.ui.animation.AnimationActivity
import com.cat.view.ui.container.ContainerActivity
import com.cat.view.ui.draw1.Draw1Activity
import com.cat.view.ui.image.ImageActivity
import com.cat.view.ui.open.OpenSourceActivity
import com.cat.view.ui.progressbar.ProgressBarActivity
import com.cat.view.ui.text.TextActivity
import com.cat.view.utils.openActivity

class MainActivity : AppCompatActivity() {

    private val list = mutableListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle ? ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        initFeature()
        val henCoderAdapter = HenCoderAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = henCoderAdapter
        recyclerView.layoutManager = staggeredGridLayoutManager
    }

    private fun initFeature() {
        var article = Article("Text View", R.drawable.ic_start_up_text)
        list.add(article)
        article = Article("Image View", R.drawable.ic_start_up_image)
        list.add(article)
        article = Article("ProgressBar", R.drawable.ic_start_up_progressbar)
        list.add(article)
        article = Article("Container Layout", R.drawable.ic_start_up_container)
        list.add(article)
        article = Article("Special Animation", R.drawable.ic_start_up_animation)
        list.add(article)
        article = Article("Open Source Libraries", R.drawable.ic_wallpaper_1)
        list.add(article)
        article = Article("Custom View 1-1 Drawing Basics", R.drawable.ic_start_up_draw1)
        list.add(article)
    }

    private inner class HenCoderAdapter : RecyclerView.Adapter<HenCoderAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(baseContext).inflate(R.layout.item_hen_coder_rv, null)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            holder.text.text = item.text
            holder.background.setImageResource(item.backgroundRes)
            holder.view.setOnClickListener { gotoFeature(item) }
        }

        private fun gotoFeature(item: Article) {
            if (item.text == "Text View") {
                openActivity<TextActivity>()
            } else if (item.text == "Container Layout") {
                openActivity<ContainerActivity>()
            } else if (item.text == "Special Animation") {
                openActivity<AnimationActivity>()
            } else if (item.text == "Custom View 1-1 Drawing Basics") {
                openActivity<Draw1Activity>()
            } else if (item.text == "Image View") {
                openActivity<ImageActivity>()
            } else if (item.text == "ProgressBar") {
                openActivity<ProgressBarActivity>()
            } else if (item.text == "Open Source Libraries") {
                openActivity<OpenSourceActivity>()
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val view: View = itemView.findViewById(R.id.item_view)
            val text: TextView = itemView.findViewById(R.id.tv_text)
            val background: ImageView = itemView.findViewById(R.id.iv_background)
        }

    }

    private data class Article(var text: String, var backgroundRes: Int)

}