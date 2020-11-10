package com.example.criminalintent


import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_crime_list.*
import java.io.File
import java.text.DateFormat
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {
    private lateinit var photoFile: File


    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }
    private var callbacks: Callbacks? = null
    private lateinit var crimeRecyclerView: RecyclerView
    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    var adapter: CrimeAdapter? = CrimeAdapter(emptyList())



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView

        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        crimeRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)



            crimeListViewModel.crimeListLiveData?.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {

                    updateUI (crimes)
                }
            }
        )

        addnewcrime.setOnClickListener {

            val crime = Crime()
            crimeListViewModel.addCrime(crime)
            callbacks?.onCrimeSelected(crime.id)
            true
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    private fun updateUI(crimes: List<Crime>) {


            adapter = CrimeAdapter(crimes)
            crimeRecyclerView.adapter = adapter

    }

    abstract class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: Crime)

    }

    private inner class NormalCrimeHolder(view: View) : CrimeHolder(view),
        View.OnClickListener {
        private lateinit var crime: Crime
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)
        private val titleTextView: TextView = itemView.findViewById(R.id.requiredcrime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.requiredcrime_date)
        private var crimeImageView: ImageView = itemView.findViewById(R.id.crime_imageView)


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
//            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()

            callbacks?.onCrimeSelected(crime.id)
        }
        override fun bind(item: Crime) {

            this.crime = item

            titleTextView.text = this.crime.title

            dateTextView.text = DateFormat.getDateInstance(DateFormat.FULL).format(this.crime.date).toString()
            solvedImageView.visibility=if(item.isSolved){
                View.VISIBLE

            }
            else
                View.GONE
            showImage(crimeImageView , crime)

        }
    }
    private  inner class SolvedCrimeHolder(view: View) : CrimeHolder(view){
        private lateinit var crime: Crime
        val requiredCrimeTextView: TextView = itemView.findViewById(R.id.requiredcrime_title)
        val requireddateTextView: TextView = itemView.findViewById(R.id.requiredcrime_date)


        override fun bind(item: Crime) {
            this.crime = item

            requiredCrimeTextView.text = this.crime.title
            requireddateTextView.text= DateFormat.getDateInstance(DateFormat.FULL).format(this.crime.date).toString()
        }

    }
    inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val SolvedCrime = 2
        val normalCrime = 1


        override fun getItemViewType(position: Int): Int {
            return if (crimes[position].isSolved == true)
                return SolvedCrime
            else
                return normalCrime

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view: View?
            var recyclerViewholder: RecyclerView.ViewHolder


            when (viewType) {
                SolvedCrime -> {

                    val view = layoutInflater.inflate(
                        R.layout.list_item_police_solved_crime,
                        parent, false
                    )

                    recyclerViewholder = SolvedCrimeHolder(view)
                }


                else -> {
                    val view = layoutInflater.inflate(R.layout.list_item_crime,
                        parent, false
                    )
                    recyclerViewholder = NormalCrimeHolder(view)
                }

            }

            return recyclerViewholder

        }


        //

        override fun getItemCount(): Int {
            if(crimes.size<=0){
                empty_view.visibility=View.VISIBLE
                addnewcrime.visibility=View.VISIBLE
            }else{
                empty_view.visibility=View.GONE
                addnewcrime.visibility=View.GONE
            }
            return crimes.size

        }


        fun sendlist(Crime:List<Crime>){
            val oldList=crimes
            val diffResult:DiffUtil.DiffResult=DiffUtil.calculateDiff(
                CrimItem(
                    oldList,Crime
                )
            )



            crimes=Crime
            diffResult.dispatchUpdatesTo(this)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val crime = crimes[position]
            if (holder is SolvedCrimeHolder)
                holder.bind(crime)
            else
                if(holder is NormalCrimeHolder)
                    holder.bind(crime)





        }
    }
    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()

        }
    }

    class  CrimItem(
        var oldList:List<Crime>,
        var newList:List<Crime>


    ):DiffUtil.Callback(){
        override fun getOldListSize(): Int {
           return oldList.size
        }

        override fun getNewListSize(): Int {
           return  newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
               return  (oldList.get(oldItemPosition).id==newList.get(newItemPosition).id)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callbacks?.onCrimeSelected(crime.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun showImage(crimeImageView: ImageView, crime: Crime) {
        photoFile = crimeListViewModel.getPhotoFile(crime)
        if (photoFile.exists()) {
            var pictureUtils = PictureUtils()
            val bitmap = pictureUtils.getScaledBitmap(
                photoFile.path ,
                requireActivity()
            )
            crimeImageView.setImageBitmap(bitmap)
        } else
            crimeImageView.setImageDrawable(null)

    }




}