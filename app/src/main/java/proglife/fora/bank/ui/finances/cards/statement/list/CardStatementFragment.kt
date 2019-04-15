package proglife.fora.bank.ui.finances.cards.statement.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_card_statement.*
import proglife.fora.bank.R
import proglife.fora.bank.models.Statement
import proglife.fora.bank.ui.base.BaseFragment
import com.arellomobile.mvp.presenter.InjectPresenter


class CardStatementFragment : BaseFragment(), CardStatementView  {

    companion object {
        fun newInstance() = CardStatementFragment()
    }

    @InjectPresenter
    lateinit var mPresenter: CardStatementPresenter
    lateinit var mAdapter: CardStatementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = CardStatementAdapter(temp(), object : CardStatementAdapter.OnStatementActionListener {
            override fun onSelect(statement: Statement) {
                mPresenter.select(statement)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_statement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvStatement.layoutManager = LinearLayoutManager(context)
        //rvStatement.addItemDecoration(CardStatementDividerDecoration(ContextCompat.getDrawable(activity!!, R.drawable.drawable_divider)!!))
        rvStatement.adapter = mAdapter
    }

    private fun temp() : List<Statement> {
        var statements = ArrayList<Statement>()

        statements.add(Statement("Азбука вкуса","Супермаркет", R.mipmap.tmp_ic_vkusa,1529157150000L))
        statements.add(Statement("Перекресток", "Гипермаркет", R.mipmap.tmp_ic_perekrestok, 1529157150000L))
        statements.add(Statement("Метро", "Гипермаркет", R.mipmap.tmp_ic_metro, 1529070750000L))
        statements.add(Statement("Перекресток", "Гипермаркет", R.mipmap.tmp_ic_perekrestok,1529070750000L))
        statements.add(Statement("Пятерочка","Продуктовый магазин", R.mipmap.tmp_ic_pyatorochka,1528984350000L))
        /*statements.add(Statement("Метро",1529483609000L))
        statements.add(Statement("Азбука вкуса",1529483609000L))
        statements.add(Statement("дядушка",1529483609000L))*/



//        statements.add(Statement(1529742809000L))
//        statements.add(Statement(1529829209000L))
//        statements.add(Statement(1529915609000L))
//        statements.add(Statement(1530002009000L))
//        statements.add(Statement(1530088409000L))
//        statements.add(Statement(1530174809000L))
//        statements.add(Statement(1530261209000L))


        return statements;
    }

}