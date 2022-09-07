package com.corporation8793.activity

import android.os.Bundle
import android.widget.ListAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.corporation8793.R
import com.google.blockly.android.BlocklyActivityHelper
import com.google.blockly.android.BlocklySectionsActivity
import com.google.blockly.android.codegen.CodeGenerationRequest
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