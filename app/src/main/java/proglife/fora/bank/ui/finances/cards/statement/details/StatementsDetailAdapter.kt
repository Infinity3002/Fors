package proglife.fora.bank.ui.finances.cards.statement.details

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.li_statement_details.view.*
import proglife.fora.bank.R
import proglife.fora.bank.utils.loadImage

class StatementsDetailAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.li_statement_details, parent, false))
    }

    override fun getItemCount(): Int {
      return 5
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemHolder) {
            //holder.itemView.setOnTouchListener(SwipeListener(holder.llOverlay))

            holder.ivPhoto1.loadImage("https://media.nngroup.com/media/people/photos/IMG_2366-copy-400x400.jpg.400x400_q95_autocrop_crop_upscale.jpg"){
                circleCrop()
            }
            holder.ivPhoto2.loadImage("https://1ofdmq2n8tc36m6i46scovo2e-wpengine.netdna-ssl.com/wp-content/uploads/2016/11/Tobias-Erb_MPI-Marburg.jpg"){
                circleCrop()
            }
            holder.ivPhoto3.loadImage("https://relayfm.s3.amazonaws.com/uploads/user/avatar/66/user_avatar_katiefloyd_artwork.png"){
                circleCrop()
            }
        }
    }


    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val llOverlay = itemView.llOverlay!!
        val ivPhoto1 = itemView.ivPhoto1!!
        val ivPhoto2 = itemView.ivPhoto2!!
        val ivPhoto3 = itemView.ivPhoto3!!
    }
}