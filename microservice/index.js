const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const { v4: uuidv4 } = require('uuid');

const app = express();
app.use(cors());
app.use(bodyParser.json());

// Datos en memoria
const posts = [
  { userId: 1, id: 1, title: 'Bienvenida', body: 'Bienvenido a la API de la clínica' }
];

const profesionales = [
  { id: uuidv4(), nombre: 'Dr. Juan Pérez', especialidad: 'General', contacto: '+56912345678', descripcion: 'Consulta general' },
  { id: uuidv4(), nombre: 'Dra. Ana López', especialidad: 'Dermatología', contacto: '+56987654321', descripcion: 'Cuidado de la piel' }
];

const remedios = [
  { id: uuidv4(), nombre: 'Antiinflamatorio', descripcion: 'Uso oral', dosis: '1 cada 12h', presentacion: 'Caja 20' }
];

// Endpoints /posts
app.get('/posts', (req, res) => res.json(posts));

// Endpoints profesionales
app.get('/profesionales', (req, res) => res.json(profesionales));
app.post('/profesionales', (req, res) => {
  const dto = req.body;
  const nuevo = { id: uuidv4(), ...dto };
  profesionales.unshift(nuevo);
  res.status(201).json(nuevo);
});
app.delete('/profesionales/:id', (req, res) => {
  const { id } = req.params;
  const idx = profesionales.findIndex(p => p.id === id);
  if (idx === -1) return res.status(404).json({ message: 'No encontrado' });
  profesionales.splice(idx, 1);
  res.status(204).send();
});

// Endpoints remedios
app.get('/remedios', (req, res) => res.json(remedios));
app.post('/remedios', (req, res) => {
  const dto = req.body;
  const nuevo = { id: uuidv4(), ...dto };
  remedios.unshift(nuevo);
  res.status(201).json(nuevo);
});
app.delete('/remedios/:id', (req, res) => {
  const { id } = req.params;
  const idx = remedios.findIndex(r => r.id === id);
  if (idx === -1) return res.status(404).json({ message: 'No encontrado' });
  remedios.splice(idx, 1);
  res.status(204).send();
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Microservicio corriendo en http://localhost:${PORT}`));

