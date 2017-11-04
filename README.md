# BusOnDemand
Aplicativo com a função de tornar mais eficiente o transporte público, unindo a necessidade dos usuários com a demanda das transportadoras.

Cada usuário faz uma solicitação de transporte para alguma linha e sentido em algum horário específico. Essas informações vão então para o servidor e a empresa tem acesso a quantos usuários estão requisitando o ônibus em determinado período. Desse modo, ela pode dimensionar melhor os ônibus (evitando a superlotação) e distribuir melhor eles de acordo com os períodos do dia.

Principais recursos:
- Interface com três botões (solicitar, avisar que pegou o ônibus, cancelar solicitação);
- Firebase para comunicação e controle de quantos usuários estão interessados;
- Tarefa periódica em background para análise do tempo;

O firebase é atualizado instantaneamente.

Projeto para o Hackaton i9.

