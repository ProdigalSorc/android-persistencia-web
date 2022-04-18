package br.com.alura.estoque.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import br.com.alura.estoque.R;
import br.com.alura.estoque.asynctask.BaseAsyncTask;
import br.com.alura.estoque.database.EstoqueDatabase;
import br.com.alura.estoque.database.dao.ProdutoDAO;
import br.com.alura.estoque.model.Produto;
import br.com.alura.estoque.retrofit.EstoqueRetrofit;
import br.com.alura.estoque.retrofit.service.ProdutoService;
import br.com.alura.estoque.ui.dialog.EditaProdutoDialog;
import br.com.alura.estoque.ui.dialog.SalvaProdutoDialog;
import br.com.alura.estoque.ui.recyclerview.adapter.ListaProdutosAdapter;
import retrofit2.Call;
import retrofit2.Response;

public class ListaProdutosActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR = "Lista de produtos";
    private ListaProdutosAdapter adapter;
    private ProdutoDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);
        setTitle(TITULO_APPBAR);

        configuraListaProdutos();
        configuraFabSalvaProduto();

        EstoqueDatabase db = EstoqueDatabase.getInstance(this);
        dao = db.getProdutoDAO();

        buscaProdutos();
    }

    private void buscaProdutos() {
        ProdutoService service = new EstoqueRetrofit().getProdutoService();
        Call<List<Produto>> call = service.buscaTodos();

        new BaseAsyncTask<>(() -> {
            try {
                Response<List<Produto>> response = call.execute();
                List<Produto> produtosNovos = response.body();
                dao.salva(produtosNovos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return dao.buscaTodos();
        }, produtosNovos -> {
            if (produtosNovos != null) {
                adapter.atualiza(produtosNovos);
            } else {
                Toast.makeText(this, "Não foi possível buscar os dados da API", Toast.LENGTH_LONG).show();
            }
        }).execute();

//        new BaseAsyncTask<>(dao::buscaTodos,
//                resultado -> adapter.atualiza(resultado))
//                .execute();
    }

    private void configuraListaProdutos() {
        RecyclerView listaProdutos = findViewById(R.id.activity_lista_produtos_lista);
        adapter = new ListaProdutosAdapter(this, this::abreFormularioEditaProduto);


        listaProdutos.setHasFixedSize(false);
        listaProdutos.setLayoutManager(new GridLayoutManager(this, 2));
//        GridTestView adapter = new GridTestView(this);
        listaProdutos.setAdapter(adapter);


//
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
//
//
///**
// * Helper class to set span size for grid items based on orientation and device type
// */
//        GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                Log.e("span", "position = " + position);
//                return adapter.getItemViewType(position) == 2 ? gridLayoutManager.getSpanCount() : 1;
//            }
//        };
//
//        gridLayoutManager.setSpanSizeLookup(onSpanSizeLookup);
//
//        listaProdutos.setLayoutManager(gridLayoutManager);

        listaProdutos.setAdapter(this.adapter);
//        this.adapter.setOnItemClickRemoveContextMenuListener(this::remove);
    }

    private void remove(int posicao,
                        Produto produtoRemovido) {
        new BaseAsyncTask<>(() -> {
            dao.remove(produtoRemovido);
            return null;
        }, resultado -> adapter.remove(posicao))
                .execute();
    }

    private void configuraFabSalvaProduto() {
        FloatingActionButton fabAdicionaProduto = findViewById(R.id.activity_lista_produtos_fab_adiciona_produto);
        fabAdicionaProduto.setOnClickListener(v -> abreFormularioSalvaProduto());
    }

    private void abreFormularioSalvaProduto() {
        new SalvaProdutoDialog(this, this::salva).mostra();
    }

    private void salva(Produto produto) {
        new BaseAsyncTask<>(() -> {
            long id = dao.salva(produto);
            return dao.buscaProduto(id);
        }, produtoSalvo ->
                adapter.adiciona(produtoSalvo))
                .execute();
    }

    private void abreFormularioEditaProduto(int posicao, Produto produto) {
        new EditaProdutoDialog(this, produto,
                produtoEditado -> edita(posicao, produtoEditado))
                .mostra();
    }

    private void edita(int posicao, Produto produto) {
        new BaseAsyncTask<>(() -> {
            dao.atualiza(produto);
            return produto;
        }, produtoEditado ->
                adapter.edita(posicao, produtoEditado))
                .execute();
    }


}
