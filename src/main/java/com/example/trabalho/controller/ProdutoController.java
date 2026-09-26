package com.example.trabalho.controller;

import com.example.trabalho.model.Produto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private List<Produto> produtos = new ArrayList<>();

    private Long proximoId = 1L;

    @PostMapping
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto produto) {

        produto.setId(proximoId++);

        produtos.add(produto);

        return ResponseEntity.status(201).body(produto);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Double precoMax) {

        List<Produto> resultado = produtos.stream()

                .filter(produto ->
                        nome == null ||
                                produto.getNome()
                                        .toLowerCase()
                                        .contains(nome.toLowerCase())
                )

                .filter(produto ->
                        categoria == null ||
                                produto.getCategoria()
                                        .equalsIgnoreCase(categoria)
                )

                .filter(produto ->
                        precoMax == null ||
                                produto.getPreco() <= precoMax
                )

                .toList();

        return ResponseEntity.ok(resultado);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {

        for (Produto produto : produtos) {

            if (produto.getId().equals(id)) {
                return ResponseEntity.ok(produto);
            }
        }

        return ResponseEntity.notFound().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(
            @PathVariable Long id,
            @RequestBody Produto produtoAtualizado) {

        for (Produto produto : produtos) {

            if (produto.getId().equals(id)) {

                produto.setNome(produtoAtualizado.getNome());
                produto.setCategoria(produtoAtualizado.getCategoria());
                produto.setPreco(produtoAtualizado.getPreco());

                return ResponseEntity.ok(produto);
            }
        }

        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        Produto produtoEncontrado = null;

        for (Produto produto : produtos) {

            if (produto.getId().equals(id)) {
                produtoEncontrado = produto;
                break;
            }
        }

        if (produtoEncontrado == null) {
            return ResponseEntity.notFound().build();
        }

        produtos.remove(produtoEncontrado);

        return ResponseEntity.noContent().build();
    }
}