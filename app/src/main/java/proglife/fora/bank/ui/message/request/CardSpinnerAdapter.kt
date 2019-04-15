package proglife.fora.bank.ui.message.request

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import proglife.fora.bank.R
import android.view.LayoutInflater



class CardSpinnerAdapter(context: Context): ArrayAdapter<String>(context, R.layout.li_spiner_card) {


    override fun getCount(): Int {
        return 5
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val row = inflater.inflate(R.layout.li_spiner_card, parent,
                false)
        return row
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val row = inflater.inflate(R.layout.li_spiner_card, parent,
                false)
        return row
    }
}