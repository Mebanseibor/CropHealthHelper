package lpu.semesterseven.project.helplines

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lpu.semesterseven.project.R
import lpu.semesterseven.project.components.toolbar.HelpLinesToolBar
import lpu.semesterseven.project.databinding.ActivityHelplinesBinding
import lpu.semesterseven.project.helplines.contactlist.Contact
import lpu.semesterseven.project.helplines.contactlist.ContactListAdapter

class HelplinesActivity: AppCompatActivity() {
    // binding
    private lateinit var binding        : ActivityHelplinesBinding

    // views
    private lateinit var toolbar        : HelpLinesToolBar

    // contact list
    private lateinit var contactListRV  : RecyclerView
    private lateinit var contactListA   : ContactListAdapter
    private var contactList: ArrayList<Contact> = arrayListOf(
        Contact(18001801551, "Ministry of Agriculture & Farmers Welfare"),
        Contact(911125842419, "Dept. of Agriculture, Coop. & Farmers Welfare"),
        Contact(18001801551, "Farmers’ Portal Helpline"),
        Contact(911125843980, "NCIPM (Integrated Pest Mgmt)"),
        Contact(914422511549, "NIPHM (Plant Health Mgmt)"),
        Contact(911125843961, "CIPMC (Integrated Pest Mgmt Centres)"),
        Contact(911125842422, "Directorate Plant Protection Quarantine & Storage"),
        Contact(911125842465, "Central Insecticides Board"),
        Contact(911125843953, "ICAR (Ag Research Council)"),
        Contact(911125843951, "ICAR-Plant Protection Institute"),
        Contact(18002702290, "Kisan Call Centre"),
        Contact(911125842451, "Plant Quarantine Information System"),
        Contact(911125842484, "ICAR-Plant Protection Research"),
        Contact(911125843973, "ICAR-Indian Agricultural Research Institute"),
        Contact(911125843989, "ICAR-Service Helpline"),
        Contact(911125843995, "ICAR-Plant Disease Diagnosis Centre"),
        Contact(912025531257, "ICAR-Nat. Res. Centre for Grapes"),
        Contact(914842421977, "ICAR-Central Plantation Research"),
        Contact(911352769873, "ICAR-Inst. Soil & Water Conservation"),
        Contact(911125842430, "Central Pesticides Laboratory")
    )

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_helplines)

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        initContactList()
    }

    private fun initContactList(){
        contactListRV   = binding.contactList
        contactListA    = ContactListAdapter(this, contactList)
        contactListRV.adapter   = contactListA
        contactListRV.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        toolbar.init()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =  toolbar.onOptionsItemSelected(item)
}