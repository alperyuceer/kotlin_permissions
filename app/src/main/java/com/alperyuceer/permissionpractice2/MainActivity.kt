package com.alperyuceer.permissionpractice2



import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alperyuceer.permissionpractice2.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()
    }


    fun selectView(view: View){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_MEDIA_IMAGES)!=PackageManager.PERMISSION_GRANTED){
            //izin verilmedi
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_MEDIA_IMAGES)){
                Snackbar.make(view,"Permission Needed",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",View.OnClickListener {
                    //izin iste
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)

                }).show()

            }else{
                //izin iste
                permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
        else{
            //izin verildi
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)

        }

    }
    private fun registerLauncher(){
        activityResultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode == RESULT_OK){
                //izin verildi
                val intentFromResult = result.data
                if (intentFromResult!=null){
                    val imageData = intentFromResult.data
                    binding.imageView.setImageURI(imageData)
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
            if (result){
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }
            else{
                Toast.makeText(this@MainActivity,"Permission Denied",Toast.LENGTH_SHORT).show()

            }
        }

    }



}