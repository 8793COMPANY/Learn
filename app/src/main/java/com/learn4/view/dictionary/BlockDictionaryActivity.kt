package com.learn4.view.dictionary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.learn4.R
import com.google.blockly.android.ui.CategoryView

class BlockDictionaryActivity : AppCompatActivity() {

    lateinit var categoryView : CategoryView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_dictionary)
//        var blocklyHelper = BlocklyActivityHelper(
//            this as AppCompatActivity?, supportFragmentManager
//        )
//        categoryView = mBlocklyActivityHelper.getmCategoryView()
    }

//    override fun getToolboxContentsXmlPath(): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun getBlockDefinitionsJsonPaths(): MutableList<String> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getGeneratorsJsPaths(): MutableList<String> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getCodeGenerationCallback(): CodeGenerationRequest.CodeGeneratorCallback {
//        TODO("Not yet implemented")
//    }
//
//    override fun onCreateSectionsListAdapter(): ListAdapter {
//        TODO("Not yet implemented")
//    }
//
//    override fun onSectionChanged(oldSection: Int, newSection: Int): Boolean {
//        TODO("Not yet implemented")
//    }


}