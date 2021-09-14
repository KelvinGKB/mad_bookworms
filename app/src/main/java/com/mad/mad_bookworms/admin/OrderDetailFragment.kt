package com.mad.mad_bookworms.admin

import android.Manifest.permission
import android.R.attr
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.R
import com.mad.mad_bookworms.customer.cart.CartOrderAdapter
import com.mad.mad_bookworms.data.BookOrder
import com.mad.mad_bookworms.data.MyCartTable
import com.mad.mad_bookworms.databinding.FragmentOrderDetailBinding
import com.mad.mad_bookworms.toBitmap
import com.mad.mad_bookworms.viewModels.BookOrderViewModel
import com.mad.mad_bookworms.viewModels.BookViewModel
import com.mad.mad_bookworms.viewModels.OrderViewModel
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat
import android.os.Environment
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Build
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.mad.mad_bookworms.customer.payment.PaymentActivity
import com.mad.mad_bookworms.data.PendingOrder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.R.attr.path
import android.content.Context
import android.content.ContextWrapper
import androidx.core.content.ContextCompat.getExternalFilesDirs
import androidx.core.content.PermissionChecker
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter


class OrderDetailFragment : Fragment() {


    var pageHeight = 1120
    var pagewidth = 792
    var bmp: Bitmap? = null
    var scaledbmp:Bitmap? = null
    private val PERMISSION_REQUEST_CODE = 200
    private val STORAGE_CODE = 1001


    private lateinit var adapter: BookOrderAdapter
    private lateinit var binding: FragmentOrderDetailBinding
    private val nav by lazy { findNavController() }
    private val vm: BookOrderViewModel by activityViewModels()
    private val bokkVm: BookViewModel by activityViewModels()
    private val orderVm: OrderViewModel by activityViewModels()

    private val id by lazy { requireArguments().getString("id", "") }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {


        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.user_profile_1);
        scaledbmp = Bitmap.createScaledBitmap(bmp!!, 140, 140, false);

        binding = FragmentOrderDetailBinding.inflate(inflater, container, false)



        val adapter = BookOrderAdapter() { holder, BookOrder ->
            var price: Double
            lifecycleScope.launch {
                val f = bokkVm.get(BookOrder.book_id)
                if (f != null) {
                    price = f.price * BookOrder.qty.toDouble()
                    holder.tvBookTitle.text = f.title
                    holder.tvBookAuthor.text = f.author
                    holder.tvBookPrice.text = "RM" + "%.2f".format(f.price)
                    holder.itemImage.setImageBitmap(f.image.toBitmap())
                    holder.tvQty.text = BookOrder.qty.toString()

                }
                else{
                    Toast.makeText(requireContext(),"Book not found", Toast.LENGTH_SHORT).show()
                    nav.navigateUp()

                }

            }

        }

        binding.rvBookOrder.adapter = adapter
        binding.rvBookOrder.setHasFixedSize(true)

        lifecycleScope.launch {
            vm.getOrder(id).observe(viewLifecycleOwner) { BookOrder ->
                binding.tvTotal.text = "${BookOrder.size}"
                binding.tvTotalPrice.text = "9"

                adapter.submitList(BookOrder)
            }

            val f = orderVm.get(id)

            binding.tvTotalPrice.text = f!!.amount.toString()


        }



        binding.btnPrint.setOnClickListener {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if(checkSelfPermission(requireContext() ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED){
                    val permision = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permision, STORAGE_CODE)
                }
                else{
                    generatePDF()
                }
            }else{
                generatePDF()
            }
        }

        return binding.root
    }

    private fun generatePDF() {
        // creating an object variable
        // for our PDF document.
        val pdfDocument = PdfDocument()

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        val paint = Paint()
        val title = Paint()

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        val mypageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        val myPage = pdfDocument.startPage(mypageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        val canvas: Canvas = myPage.canvas

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp!!, 56f, 40f, paint)

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15f)

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(requireContext(), R.color.background_color))

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("A portal for IT professionals.", 209f, 100f, title)
        canvas.drawText("Geeks for Geeks", 209f, 80f, title)

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title.setColor(ContextCompat.getColor(requireContext(), R.color.background_color))
        title.setTextSize(15f)

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER)
        canvas.drawText("This is sample document which we have created.", 396f, 560f, title)

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.

//        val file = File(context?.getExternalFilesDirs("ok"), "myfile.pdf")
        //Youtube
        val cw = ContextWrapper(requireContext())
        val FileDirectory  = cw.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val mFilePath = File(FileDirectory, "myfile.pdf")

        //Geek
        val file2 = File(Environment.getExternalStorageDirectory() ,"myfile.pdf")

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(FileOutputStream(file2))
            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(requireContext(),"PDF file generated successfully", Toast.LENGTH_SHORT).show()

        } catch (e: IOException) {
            // below line is used
            // to handle error
            e.printStackTrace()
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close()
    }

    private fun savePDF(){
        val mDoc = Document()

        val cw = ContextWrapper(requireContext())
        val FileDirectory  = cw.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val mFilePath = File(FileDirectory, "myfile.pdf")

        try{
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()

            val data = "Ang Jen Tat"
            mDoc.addAuthor("Ang")
            mDoc.add(Paragraph(data))
            mDoc.close()
        }catch (e: IOException) {
            e.printStackTrace()
        }


    }


}