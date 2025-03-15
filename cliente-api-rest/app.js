const express = require('express');
const axios = require('axios');
const bodyParser = require('body-parser');
const app = express();


app.use(bodyParser.urlencoded({ extended: true}));
app.set('view engine', 'ejs');
app.use(express.static('public'));

const apiUrl = 'http://localhost:8080/api/clientes';

//rota teste

// app.get('/', (req, res) => {
//     res.sand('Criando minha primera rota');
// });


app.get('/', async(req, res) => {
    try{
        const response = await axios.get(apiUrl);
        const clientes = response.data;
        res.render('index', { clientes });
        // console.log({clientes})

    } catch(error){
        console.error(error);
        res.status(500).send('Erro ao buscar cliente')
    }
});

app.post('/excluir/:id', async(req, res) =>{
    const { id } = req.params;
    try{
        await axios.delete(`${apiUrl}/${id}`);
        res.redirect('/');
    }catch(error){
        console.error(error);
        res.status(500).send('erro ao excluir cliente');

    }
});

app.listen(3000, () => {
    console.log('Servidor rodando na porta 3000');
});
