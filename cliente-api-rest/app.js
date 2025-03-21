const express = require('express');
const axios = require('axios');
const bodyParser = require('body-parser');
const app = express();
app.use(express.json());


app.use(bodyParser.urlencoded({ extended: true}));
app.set('view engine', 'ejs');
app.use(express.static('public'));

const apiUrl = 'http://localhost:8080/api/clientes';

//rota teste

// app.get('/', (req, res) => {
//     res.sand('Criando minha primera rota');
// });

app.get('/', (req, res) => {
    res.render('index'); // Renderiza index.ejs
});

app.get('/cadastro', (req, res) => {
    res.render('cadastro')
});
app.post('/cadastrar', async(req, res) =>{
    try{
        const response = await axios.post(apiUrl, req.body);
        console.log(req.body);
        res.status(201).json(response.data);
    }catch(error){
        console.error(error);
    }
});

app.get('/listarClientes', async(req, res) => {
    try {
        const page = parseInt(req.query.page || 0);
        const size = 4;
        const search = req.query.search || '';
        
        const response = await axios.get(apiUrl, {
            params: { 
                page, 
                size,
                nome: search // Garanta que o nome do parâmetro está correto
            }
        });
        
        const clientes = response.data.content;
        const totalPages = response.data.totalPages;
        const currentPage = response.data.number;

        res.render('ListarClientes', { 
            clientes: clientes || [],
            totalPages,
            currentPage,
            searchTerm: search
        });

    } catch(error) {
        console.error('Erro detalhado:', error.response?.data);
        res.status(500).send('Erro ao buscar clientes');
    }
});

app.get('/editar/:id', async (req, res) => {
    const { id } = req.params;
    try {
        const response = await axios.get(`${apiUrl}/${id}`);
        const cliente = response.data;
        res.render('editar', { cliente });
    } catch (error) {
        console.error(error);
        res.status(500).send('Erro ao buscar cliente');
    }
});

app.post('/editar/:id', async (req, res) => {
    const { id } = req.params;
    const clienteAtualizado = req.body;

    try {
        const response = await axios.put(`${apiUrl}/${id}`, clienteAtualizado);
        res.json({ message: "Cliente atualizado com sucesso" }); // Retorno JSON para o frontend
    } catch (error) {
        console.error("Erro ao editar cliente:", error.response?.data || error.message);
        res.status(error.response?.status || 500).json({ error: error.response?.data || "Erro ao editar cliente" });
    }
});


app.post('/excluir/:id', async(req, res) =>{
    const { id } = req.params;
    try{
        await axios.delete(`${apiUrl}/${id}`);
        res.redirect("/listarClientes?mensagem=Usuário excluído com sucesso");
    }catch(error){
        console.error(error);
        res.status(500).send('erro ao excluir cliente');

    }
});

app.listen(3000, () => {
    console.log('Servidor rodando na porta 3000');
});
